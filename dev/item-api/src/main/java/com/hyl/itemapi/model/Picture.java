package com.hyl.itemapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@Table(name = "picture")
public class Picture {


    //************************************************** GROUPES DE VALIDATION
    public interface AddValidation {}
    public interface AfterAddFileValidation {}


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_picture")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "{hyl.picture.id.error.null}", groups = {AddValidation.class})
    private Long id;

    @NotBlank(message = "{hyl.picture.name.error.notblank}", groups = {AddValidation.class})
    @Length(min = 1, max = 15, message = "{hyl.picture.name.error.length}", groups = {AddValidation.class})
    @Column(name = "name")
    private String name;

    @NotBlank(message = "{hyl.picture.url.error.notblank}", groups = {AfterAddFileValidation.class})
    @Column(name = "url")
    private String url;

    @NotNull(message = "{hyl.picture.subitem.error.notnull}", groups = {AddValidation.class})
    @ManyToOne(targetEntity = SubItem.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subitem")
    @JsonIgnore
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
