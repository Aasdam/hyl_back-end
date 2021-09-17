package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.ItemDao;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Category;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {


    //************************************************** DAO
    private static ItemDao itemDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public ItemService(ItemDao itemDao) {
        ItemService.itemDao = itemDao;
    }


    //************************************************** METHODES
    public static List<Item> getItemsByIdUser(long id) {
        return itemDao.findAllByIdUser(id);
    }

    protected static List<Item> getItemsWihtoutCategoryByIdUser(long id) {
        return itemDao.findAllByIdUserAndCategoryIsNull(id);
    }

    public static void renameItem(long idItem, String name) {
        Item item = getItemById(idItem);
        if (!item.getName().equals(name)) {
            item.setName(name);
            CustomValidator.validate(item, Item.UpdateValidation.class);
            itemDao.save(item);
        }
    }

    public static void moveItem(long idItem, Long idCategoryDest) {
        Item item = getItemById(idItem);
        Category oldCategory = item.getCategory();
        if (idCategoryDest == null || idCategoryDest == 0) item.setCategory(null);
        else item.setCategory(CategoryService.getCategoryById(idCategoryDest));
        if(item.getCategory() != oldCategory) {
            CustomValidator.validate(item, Item.MoveValidation.class);
            itemDao.save(item);
        }
    }

    public static void save(Item item) {
        itemDao.save(item);
    }

    public static void deleteItem(long id, String token, long idUser) {
        Item item = getItemById(id);
        CustomValidator.validate(item, Item.OwnerValidation.class);
        List<SubItem> subItems = new ArrayList<>(item.getSubItems());
        subItems.forEach(subItem -> {
            item.getSubItems().remove(subItem);
            SubItemService.deleteSubItem(subItem, token, idUser);
        });
        itemDao.delete(item);
        FileService.deleteFile(item);
    }

    public static Item addItem(String name, String description, long idCategory,
                        String reference, long idUser, List<MultipartFile> files) {

        // Construction de l'item
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setIdUser(idUser);
        item.setSubItems(new ArrayList<>());
        if (idCategory != 0) item.setCategory(CategoryService.getCategoryById(idCategory));

        // Construction du subitem
        item.getSubItems().add(SubItemService.buildSubItem(item, reference, files));

        // Validation du nouvel item et de ses composant
        validAddItem(item);

        // Enregistrement du nouvel item
        item =  itemDao.save(item);

        // Enregistrement des images et mise à jour en bdd
        if (files != null) {
            item.getSubItems().forEach(subItem -> {
                Hashtable<String, String> urlTable = FileService.saveMultipartFile(subItem, files);
                PictureService.majUrlPicture(urlTable);
            });
        }

        // Retour
        return item;
    }

    /**
     * Validation du nouvel item et de ses composants avant l'ajout en bdd
     * @param item Le nouvel item
     */
    private static void validAddItem(Item item) {
        CustomValidator.validate(item, Item.AddValidation.class);
        item.getSubItems().forEach(SubItemService::validAddSubItem);
    }

    public static boolean checkAtomicName(String name, long idUser) {
        return itemDao.findByNameAndIdUser(name, idUser).isEmpty();
    }

    protected static Item getItemById(long id) {
        Optional<Item> optItem = itemDao.findById(id);
        if ( optItem.isPresent() ) return optItem.get();
        else throw new CustomNotFoundException("L'objet Item avec pour id="+id+" n'a pas été trouvé.");
    }
}
