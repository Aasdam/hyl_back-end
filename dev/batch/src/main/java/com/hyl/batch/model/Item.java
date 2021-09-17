package com.hyl.batch.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item", schema = "item")
public class Item {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_item")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(targetEntity = SubItem.class, mappedBy = "item", fetch = FetchType.EAGER)
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
                ", subItems=" + subItems +
                ", idUser=" + idUser +
                '}';
    }
}
