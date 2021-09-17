package com.hyl.batch.model;

import javax.persistence.*;

@Entity
@Table(name = "picture", schema = "item")
public class Picture {

    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_picture")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @ManyToOne(targetEntity = SubItem.class)
    @JoinColumn(name = "id_subitem")
    private SubItem subItem;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SubItem getSubItem() {
        return subItem;
    }

    public void setSubItem(SubItem subItem) {
        this.subItem = subItem;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", subItem.id=" + subItem.getId() +
                '}';
    }
}
