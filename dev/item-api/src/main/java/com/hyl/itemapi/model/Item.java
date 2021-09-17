package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyl.itemapi.model.constraint.AtomicItemNameConstraint;
import com.hyl.itemapi.model.constraint.CharacterRepetitionConstraint;
import com.hyl.itemapi.model.constraint.IdOwnerConstraint;
import com.hyl.itemapi.model.constraint.NoWhiteSpaceConstraint;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@IdOwnerConstraint(groups = {Item.UpdateValidation.class, Item.MoveValidation.class, Item.OwnerValidation.class})
@Entity
@Table(name = "item")
public class Item {


    //************************************************** GROUPE DE VALIDATION
    public interface AddValidation {}
    public interface UpdateValidation {}
    public interface MoveValidation {}
    public interface OwnerValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_item")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "{hyl.item.id.error.null}", groups = {AddValidation.class})
    private Long id;

    @NotBlank(message = "{hyl.item.name.error.notblank}", groups = {AddValidation.class, UpdateValidation.class})
    @Length(min = 3, max = 15, message = "{hyl.item.name.error.length}", groups = {AddValidation.class, UpdateValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.item.name.error.characterrepetition}", groups = {AddValidation.class, UpdateValidation.class})
    @NoWhiteSpaceConstraint(message = "{hyl.item.name.error.nowhitespace}", groups = {AddValidation.class, UpdateValidation.class})
    @AtomicItemNameConstraint(groups = {AddValidation.class, UpdateValidation.class})
    @Column(name = "name")
    private String name;

    @Length(max = 50, message = "{hyl.item.description.error.length}", groups = {AddValidation.class})
    @Column(name = "description")
    private String description;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    @JsonIgnore
    private Category category;

    @Size(min = 1, message = "{hyl.item.subitems.error.size}", groups = {AddValidation.class})
    @OneToMany(targetEntity = SubItem.class, mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubItem> subItems;

    @Column(name = "id_usager")
    private Long idUser;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<SubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<SubItem> subItems) {
        this.subItems = subItems;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", subItems=" + subItems +
                ", idUser=" + idUser +
                '}';
    }
}
