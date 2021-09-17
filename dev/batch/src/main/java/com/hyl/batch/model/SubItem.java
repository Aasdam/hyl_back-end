package com.hyl.batch.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subitem", schema = "item")
public class SubItem {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_subitem")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "id_item")
    private Item item;

    @OneToMany(targetEntity = Picture.class, mappedBy = "subItem", fetch = FetchType.EAGER)
    private List<Picture> urlImages;


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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Picture> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(List<Picture> urlImages) {
        this.urlImages = urlImages;
    }

    //************************************************** TO STRING
    @Override
    public String toString() {
        return "SubItem{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", item.id=" + item.getId() +
                ", urlImages=" + urlImages +
                '}';
    }

}
