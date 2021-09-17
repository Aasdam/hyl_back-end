package com.hyl.batch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usager", schema = "usager")
public class User {


    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_usager")
    private Long id;

   @Column(name = "name")
    private String name;

   @Column(name = "surname")
    private String surname;

   @Column(name = "email", unique = true)
    private String email;

    @Column(name = "civility")
    private String civility;

   @Column(name = "cellphone", unique = true)
    private String cellphone;


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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCivility() {
        return civility;
    }

    public void setCivility(String civility) {
        this.civility = civility;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", civility='" + civility + '\'' +
                ", cellphone='" + cellphone + '\'' +
                '}';
    }
}
