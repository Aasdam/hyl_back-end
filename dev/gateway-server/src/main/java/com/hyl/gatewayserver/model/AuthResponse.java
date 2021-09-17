package com.hyl.gatewayserver.model;

public class AuthResponse {


    // ********************************************* PARAMETRES
    private String idUser;
    private String token;


    // ********************************************* CONSTRUCTEURS
    public AuthResponse() {
    }

    public AuthResponse(String token, String idUser) {
        this.idUser = idUser;
        this.token = token;
    }


    // ********************************************* GETTERS / SETTERS
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    // ********************************************* TO STRING
    @Override
    public String toString() {
        return "AuthResponse{" +
                "idUser='" + idUser + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
