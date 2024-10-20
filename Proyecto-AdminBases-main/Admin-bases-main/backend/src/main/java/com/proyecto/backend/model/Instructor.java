package com.proyecto.backend.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cod_instructor;
    
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String direccion;
    private String e_mail;
    private String tel_cel;
    private String tel_habitacion;
    
    @Temporal(TemporalType.DATE)
    private Date fecha_contratacion;

    // Getters y Setters

    public int getCod_instructor() {
        return cod_instructor;
    }

    public void setCod_instructor(int cod_instructor) {
        this.cod_instructor = cod_instructor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getTel_cel() {
        return tel_cel;
    }

    public void setTel_cel(String tel_cel) {
        this.tel_cel = tel_cel;
    }

    public String getTel_habitacion() {
        return tel_habitacion;
    }

    public void setTel_habitacion(String tel_habitacion) {
        this.tel_habitacion = tel_habitacion;
    }

    public Date getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(Date fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }
}
