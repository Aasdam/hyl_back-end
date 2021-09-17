package com.hyl.gatewayserver.model;

public class SignUpRequest {

    private String email;
    private String pseudo;
    private String surname;
    private String name;
    private String civility;
    private String cellphone;
    private String password;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", civility='" + civility + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
