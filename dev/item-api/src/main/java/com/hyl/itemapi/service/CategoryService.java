package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.CategoryDao;
import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Category;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    //************************************************** LOGGER
    static Logger logger = LoggerFactory.getLogger(CategoryService.class);


    //************************************************** BEAN
    private static String categoryMaxAuthorizedDepth;


    //************************************************** DAO
    private static CategoryDao categoryDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        CategoryService.categoryDao = categoryDao;
    }


    //************************************************** SETTER
    @Value("${hyl.constraint.category.depth}")
    public void setCategoryMaxAuthorizedDepth(String categoryMaxAuthorizedDepth) {
        CategoryService.categoryMaxAuthorizedDepth = categoryMaxAuthorizedDepth;
    }


    //************************************************** METHODES
    public static Category getMainCategory(long idUser) {
        Category category = new Category();
        category.setCategories(categoryDao.findAllByIdUserAndCategoryParentIsNull(idUser));
        category.setItems(ItemService.getItemsWihtoutCategoryByIdUser(idUser));
        return category;
    }

    public static Category getCategoryById(long idCategory) {
        Optional<Category> optCategory = categoryDao.findById(idCategory);
        if (optCategory.isPresent()) return optCategory.get();
        else throw new CustomNotFoundException("La catégorie ayant pour id '"+idCategory+"' est introuvable.");
    }

    public static void renameCategory(long idCategory, String name) {
        Category category = getCategoryById(idCategory);
        if (!category.getName().equals(name)) {
            category.setName(name);
            CustomValidator.validate(category, Category.UpdateValidation.class);
            categoryDao.save(category);
        }
    }

    public static void moveCategory(long idCategory, Long idCategoryDest, boolean skip) {
        Category category = getCategoryById(idCategory);
        Category categoryDest = idCategoryDest != null && idCategoryDest != 0 ? getCategoryById(idCategoryDest) : null;
        if (!skip) CustomValidator.validate(category, Category.OwnerValidation.class);
        if (!skip && categoryDest != null) CustomValidator.validate(categoryDest, Category.OwnerValidation.class);
        checkCategoryDepthConstraintException(category, categoryDest, false);
        category.setCategoryParent(categoryDest);
        if (!skip && categoryDest != null) {
            categoryDest.getCategories().add(category);
            categoryDao.save(categoryDest);
        } else if (!skip) {
            categoryDao.save(category);
        }
    }

    public static Category addChildCategory(String name, Long idParent, Long idUser, boolean skip) {
        Category parentCategory = idParent != null && idParent!= 0 ? getCategoryById(idParent) : null;
        Category category = new Category();
        category.setName(name);
        category.setIdUser(idUser);
        category.setCategoryParent( idParent != null && idParent!= 0 ? getCategoryById(idParent) : null);
        if (!skip) CustomValidator.validate(category, Category.AddChildValidation.class);
        checkCategoryDepthConstraintException(null, parentCategory, false);
        if (!skip) category = categoryDao.save(category);
        return category;
    }

    public static Category addParentCategory(String name, long idChild, Long idUser, boolean skip) {
        Category category = new Category();
        Category oldParentCategory = getCategoryById(idChild);
        category.setName(name);
        category.setIdUser(idUser);
        category.setCategoryParent(oldParentCategory.getCategoryParent());
        category.setCategories(new ArrayList<>());
        category.getCategories().add(oldParentCategory);
        oldParentCategory.setCategoryParent(category);
        checkCategoryDepthConstraintException(oldParentCategory, category, true);
        if (!skip) category = categoryDao.save(category);
        return category;
    }

    public static void deleteCategory(long id) {
        Category category = getCategoryById(id);
        CustomValidator.validate(category, Category.OwnerValidation.class);
        Category parentCategory = category.getCategoryParent();

        category.getCategories().forEach(category1 -> {
            category1.setCategoryParent(parentCategory);
            if (parentCategory != null) parentCategory.getCategories().add(category1);
            else categoryDao.save(category1);
        });

        category.getItems().forEach(item -> {
            item.setCategory(parentCategory);
            if (parentCategory != null) parentCategory.getItems().add(item);
            else ItemService.save(item);
        });

        if (parentCategory != null) categoryDao.save(parentCategory);
        categoryDao.delete(category);
    }

    public static boolean checkAtomicName(String name, long idUser) {
        return categoryDao.findByNameAndIdUser(name, idUser).isEmpty();
    }

    public static boolean checkCategoryDepthConstraintForFrontAsyncValidator
                    (Long idCategoryToMove, Long idTargetCategory, String type) {

        if (type == null || (!type.equals("MOVE") && idTargetCategory == null)) return true;
        switch (type) {
            case "MOVE":
                try {
                    moveCategory(idCategoryToMove, idTargetCategory, true);
                    return false;
                } catch (CustomBadRequestException ignored) {
                    return true;
                }
            case "ADD_PARENT":
                try {
                    addParentCategory("", idTargetCategory, null, true);
                    return false;
                } catch (CustomBadRequestException ignored) {
                    return true;
                }
            case "ADD_CHILD":
                try {
                    addChildCategory("", idTargetCategory, null, true);
                    return false;
                } catch (CustomBadRequestException ignored) {
                    return true;
                }
            default:
                return true;
        }
    }

    private static void checkCategoryDepthConstraintException(Category categoryToMove, Category targetCategory, boolean skipHierarchical) {
        if (checkCategoryDepthConstraint(categoryToMove, targetCategory, skipHierarchical))
            throw new CustomBadRequestException("Les contraintes de gestion des catégories ne sont pas respectés");
    }

    public static boolean checkCategoryDepthConstraint(Category categoryToMove, Category targetCategory, boolean skipHierarchical) {
        // Retour true -> invalide
        // Retour false -> valide

        // ************************************* Classe interne générale
        class Interne {

            // Vérifie le lien hierarchique lors d'un déplacement de catégorie -> Enfant + ascendant direct -> interdit
            boolean isValidHierarchicalLink(Category categoryToMove, Category targetCategory) {
                class Interne1 {
                    void recursivity (Category category1, Category category2) {
                        if (category1 != null && category1.getCategories() != null) {
                            for (Category category : category1.getCategories()) {
                                if (category.equals(category2)) throw new RuntimeException();
                                recursivity(category, category2);
                            }
                        }
                    }
                }
                if ( categoryToMove.getCategoryParent() == targetCategory) {
                    return false;
                }
                try {
                    new Interne1().recursivity(categoryToMove, targetCategory);
                } catch (RuntimeException e) {
                    return false;
                }
                return true;
            }

            // Vérifie si la catégorie de destination (targetCategory) est un ascendant au xieme degré de la catégorie à déplacer
            // Si tel est le cas -> Pas de validation au niveau des profondeur (ascendant direct = interdit)
            boolean isHierarchicalAscendantLink(Category categoryToMove, Category targetCategory) {
                Category parentCategory = categoryToMove.getCategoryParent();
                if (parentCategory == null) return false;
                else parentCategory = parentCategory.getCategoryParent();

                while (true) {
                    if (parentCategory == null) return false;
                    if (targetCategory.equals(parentCategory)) return true;
                    else {
                        parentCategory = parentCategory.getCategoryParent();
                    }
                }
            }

            // Vérifie la profondeur en calculant le nombre d'ascendant de la catégorie cible
            // et la profondeur (maximale si plusieurs enfants) de la catégorie à déplacer
            boolean checkCategoryDepth(Category categoryToMove, Category targetCategory) {
                class Interne2 {
                    private int checkByRecursivity (Category pCategory, int counter) {
                        if (pCategory.getCategories() != null) {
                            for (Category category : pCategory.getCategories()) {
                                counter ++;
                                counter = this.checkByRecursivity(category, counter);
                            }
                        }
                        return counter;
                    }
                }
                Interne2 interne = new Interne2();
                int finalCount = 0;
                if (categoryToMove != null && categoryToMove.getCategories() != null) {
                    for (Category category : categoryToMove.getCategories()) {
                        int count0 = 1;
                        count0 = interne.checkByRecursivity(category, count0);
                        if (count0 > finalCount) finalCount = count0;
                    }
                }
                Category parentCategory = targetCategory.getCategoryParent();
                int count = 0;
                if (parentCategory != null) {
                    count++;
                    while (true) {
                        if (parentCategory.getCategoryParent() == null) break;
                        else {
                            parentCategory = parentCategory.getCategoryParent();
                            count++;
                        }
                    }
                }
                return count + finalCount >= Integer.parseInt(categoryMaxAuthorizedDepth);
            }
        }

        //************************************ Partie controleur de la grand méthode
        Interne interne = new Interne();

        // Dans le cas d'un déplacement de catégorie ou de l'ajout d'une catégorie parente
        if (targetCategory != null && categoryToMove != null) {

            // Si déplacement -> Vérification du lien hierarchique
            // Sinon on skip -> lors de l'ajout d'une catégorie parente, il y a forcément lien hierarchique
            if (interne.isValidHierarchicalLink(categoryToMove, targetCategory) || skipHierarchical) {

                // Si ascendant au xième degré, pas de problème de profondeur
                if (interne.isHierarchicalAscendantLink(categoryToMove, targetCategory)) {
                    return false;

                // Sinon on check
                } else {
                    return interne.checkCategoryDepth(categoryToMove, targetCategory);
                }
            } else {
                return true;
            }

            // Dans le cas d'un ajout de catégorie enfant
        } else if (targetCategory != null) {
            return interne.checkCategoryDepth(categoryToMove, targetCategory);

            // Valeur par défaut obligatoire
        } else {
            return false;
        }
    }
}
