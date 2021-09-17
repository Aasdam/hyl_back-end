package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyl.itemapi.model.constraint.AtomicCategoryNameConstraint;
import com.hyl.itemapi.model.constraint.CharacterRepetitionConstraint;
import com.hyl.itemapi.model.constraint.IdOwnerConstraint;
import com.hyl.itemapi.model.constraint.NoWhiteSpaceConstraint;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@IdOwnerConstraint(groups = {Category.UpdateValidation.class, Category.OwnerValidation.class})
@Entity
@Table(name = "category")
public class Category {


    //************************************************** GROUPE DE VALIDATION
    public interface AddChildValidation {}
    public interface AddParentValidation {}
    public interface UpdateValidation {}
    public interface OwnerValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_category")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "{hyl.category.id.error.null}", groups = {AddChildValidation.class, AddParentValidation.class})
    private Long id;

    @NotBlank(message = "{hyl.category.name.error.notblank}", groups = {AddChildValidation.class, AddParentValidation.class, UpdateValidation.class})
    @Length(min = 3, max = 15, message = "{hyl.category.name.error.length}", groups = {AddChildValidation.class, AddParentValidation.class, UpdateValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.category.name.error.characterrepetition}", groups = {AddChildValidation.class, AddParentValidation.class, UpdateValidation.class})
    @NoWhiteSpaceConstraint(message = "{hyl.category.name.error.nowhitespace}", groups = {AddChildValidation.class, AddParentValidation.class, UpdateValidation.class})
    @AtomicCategoryNameConstraint(groups = {AddChildValidation.class, AddParentValidation.class, UpdateValidation.class})
    @Column(name = "name")
    private String name;

    @Column(name = "id_usager")
    private Long idUser;

    @Size(max = 0, message = "{hyl.category.categories.error.size}", groups = {AddChildValidation.class})
    @Size(min=1, max = 1, message = "{hyl.category.categories.error.size}", groups = {AddParentValidation.class})
    @OneToMany(targetEntity = Category.class, mappedBy = "categoryParent", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Category> categories;

    @Size(max = 0, message = "{hyl.category.items.error.size}", groups = {AddChildValidation.class, AddParentValidation.class})
    @OneToMany(targetEntity = Item.class, mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Item> items;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category_parent")
    @JsonIgnore
    private Category categoryParent;


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Category getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(Category categoryParent) {
        this.categoryParent = categoryParent;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name +
                ", idUser=" + idUser +
                ", categories=" + categories +
                ", items=" + items +
                ", categoryParent.id=" + (categoryParent != null ? categoryParent.getId() : null)+
                '}';
    }
}
