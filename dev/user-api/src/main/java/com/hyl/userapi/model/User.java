package com.hyl.userapi.model;

import com.hyl.userapi.model.constraint.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "usager")
public class User {
    
    //************************************************** GROUPE DE VALIDATION
    public interface AuthenticateValidation {}
    public interface RegisterValidation {}
    public interface UpdateValidation {}

    
    //************************************************** PARAMETRES
    @Id
    @Column(name = "id_usager")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{hyl.user.name.error.notblank}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Length(min = 3, max = 15, message = "{hyl.user.name.error.length}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Pattern(regexp = "[a-zA-Z-]*", message = "{hyl.user.name.error.pattern}", groups = {RegisterValidation.class, UpdateValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.user.name.error.characterrepetition}", groups = {RegisterValidation.class, UpdateValidation.class})
    @NoWhiteSpaceConstraint(message = "{hyl.user.name.error.nowhitespace}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Column(name = "name")
    private String name;

    @NotBlank(message = "{hyl.user.surname.error.notblank}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Length(min = 3, max = 30, message = "{hyl.user.surname.error.length}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Pattern(regexp = "[a-zA-Z -]*", message = "{hyl.user.surname.error.pattern}", groups = {RegisterValidation.class, UpdateValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.user.surname.error.characterrepetition}", groups = {RegisterValidation.class, UpdateValidation.class})
    @NoWhiteSpaceConstraint(message = "{hyl.user.surname.error.nowhitespace}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Column(name = "surname")
    private String surname;

    @NotBlank(message = "{hyl.user.email.error.notblank}", groups = {AuthenticateValidation.class, RegisterValidation.class, UpdateValidation.class})
    @Length(min = 10, max = 50, message = "{hyl.user.email.error.length}", groups = {AuthenticateValidation.class, RegisterValidation.class, UpdateValidation.class})
    @Pattern(regexp = "^[a-z0-9._-]{3,99}@[a-z0-9._-]{3,99}.[a-z]{2,}$", message = "{hyl.user.email.error.pattern}", groups = {AuthenticateValidation.class, RegisterValidation.class, UpdateValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.user.email.error.characterrepetition}", groups = {AuthenticateValidation.class, RegisterValidation.class, UpdateValidation.class})
    @AtomicEmailConstraint(groups = {RegisterValidation.class})
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "{hyl.user.pseudo.error.notblank}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Length(min = 3, max = 15, message = "{hyl.user.pseudo.error.length}", groups = {RegisterValidation.class, UpdateValidation.class})
    @NoWhiteSpaceConstraint(message = "{hyl.user.pseudo.error.nowhitespace}", groups = {RegisterValidation.class, UpdateValidation.class})
    @CharacterRepetitionConstraint(message = "{hyl.user.pseudo.error.characterrepetition}", groups = {RegisterValidation.class, UpdateValidation.class})
    @Column(name = "pseudo")
    private String pseudo;

    @CivilityConstraint(groups = {RegisterValidation.class, UpdateValidation.class})
    @Column(name = "civility")
    private String civility;

    @CellphoneConstraint(groups = {RegisterValidation.class, UpdateValidation.class})
    @AtomicCellphoneConstraint(groups = {RegisterValidation.class})
    @Column(name = "cellphone", unique = true)
    private String cellphone;

    @NotBlank(message = "{hyl.user.password.error.notblank}", groups = {AuthenticateValidation.class, RegisterValidation.class, UpdateValidation.class})
    @Length(min = 8, max = 25, message = "{hyl.user.password.error.length}", groups = {AuthenticateValidation.class, RegisterValidation.class, UpdateValidation.class})
    @Column(name = "password")
    private String password;

    
    //************************************************** CONSTRUCTEURS
    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public User(String email, String name, String surname, String pseudo, 
                String civility, String cellphone, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.pseudo = pseudo;
        this.civility = civility;
        this.cellphone = cellphone;
        this.password = password;
    }


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

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //************************************************** TO STRING
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", civility='" + civility + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
