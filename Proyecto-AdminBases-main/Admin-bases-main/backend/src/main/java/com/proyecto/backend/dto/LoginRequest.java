package com.proyecto.backend.dto;

public class LoginRequest {

    private Integer cedula;
    private String password;
    private String username;

    // Construct vacío (necesario si usas frameworks que requieren un constructor por defecto)
    public LoginRequest() {}

    // Constructor con parámetros
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public Integer getCedula() {
        return cedula;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    // Setters
    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
