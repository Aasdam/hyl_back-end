package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyl.itemapi.model.constraint.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@MaxSubItemConstraint(groups = {SubItem.AddValidation.class})
@Entity
@Table(name = "subitem")
public class SubItem {


    //************************************************** GROUPE DE VALIDATION
    public interface AddValidation {}
    public interface UpdateValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_subitem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "{hyl.subitem.id.error.null}", groups = {AddValidation.class})
    private Long id;

    @NotBlank(message = "{hyl.subitem.reference.error.notblank}", groups = {AddValidation.class, UpdateValidation.class})
    @Length(min = 6, max = 15, message = "{hyl.subitem.reference.error.length}", groups = {AddValidation.class, UpdateValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.subitem.reference.error.characterrepetition}", groups = {AddValidation.class, UpdateValidation.class})
    @NoWhiteSpaceConstraint(message = "{hyl.subitem.reference.error.nowhitespace}", groups = {AddValidation.class, UpdateValidation.class})
    @AtomicSubItemRefConstraint(groups = {AddValidation.class, UpdateValidation.class})
    @Column(name = "reference")
    private String reference;

    @Size(max = 0, message = "{hyl.subitem.trackingsheets.error.size}", groups = {AddValidation.class})
    @OneToMany(targetEntity = TrackingSheet.class, mappedBy = "subItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
    private List<TrackingSheet> trackingSheets;

    @OneToMany(targetEntity = Picture.class, mappedBy = "subItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Picture> urlImages;

    @IdOwnerConstraint(groups = {AddValidation.class, UpdateValidation.class})
    @NotNull(message = "{hyl.subitem.item.error.notnull}", groups = {AddValidation.class})
    @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item")
    @JsonIgnore
    private Item item;


    //************************************************** GETTERS / SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<TrackingSheet> getTrackingSheets() {
        return trackingSheets;
    }

    public void setTrackingSheets(List<TrackingSheet> trackingSheets) {
        this.trackingSheets = trackingSheets;
    }

    public List<Picture> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(List<Picture> urlImages) {
        this.urlImages = urlImages;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "SubItem{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", trackingSheets=" + trackingSheets +
                ", urlImages=" + urlImages +
                ", item.id=" + item.getId() +
                '}';
    }
}
