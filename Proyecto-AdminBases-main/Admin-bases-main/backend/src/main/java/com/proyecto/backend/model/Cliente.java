package com.proyecto.backend.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "clientes")
public class Cliente {
    
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)  // Genera automáticamente el ID
    @Id
    private int cedula;
    
    //@Column(name = "NOMBRE")
    private String nombre;

    //@Column(name = "APELLIDO1")
    private String apellido1;

//@Column(name = "APELLIDO2")
    private String apellido2;

    //@Column(name = "DIRECCION")
    private String direccion;

    //@Column(name = "E_MAIL")
    private String e_mail;
    
    //@Column(name = "FECHA_INSCRIPCION")
    @Temporal(TemporalType.DATE)
    private Date fecha_inscripcion;

    //@Column(name = "CELULAR")
    private Long celular;

    //@Column(name = "TEL_HABITACION")
    private Long tel_habitacion;

    //@Column(name = "PASSWORD", nullable = false)
    private String password;

    // Constructor vacío (necesario para JPA)
    public Cliente() {
    }

    // Constructor sin ID (se generará automáticamente)
    public Cliente(String nombre, String apellido1, String apellido2, String direccion, String e_mail, Date fecha_inscripcion, Long celular, Long tel_habitacion, String password) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.direccion = direccion;
        this.e_mail = e_mail;
        this.fecha_inscripcion = fecha_inscripcion;
        this.celular = celular;
        this.tel_habitacion = tel_habitacion;
        this.password = password;
    }

    // Getters y Setters

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
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

    public Date getFecha_inscripcion() {
        return fecha_inscripcion;
    }

    public void setFecha_inscripcion(Date fecha_inscripcion) {
        this.fecha_inscripcion = fecha_inscripcion;
    }

    public Long getCelular() {
        return celular;
    }

    public void setCelular(Long celular) {
        this.celular = celular;
    }

    public Long getTel_habitacion() {
        return tel_habitacion;
    }

    public void setTel_habitacion(Long tel_habitacion) {
        this.tel_habitacion = tel_habitacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cedula=" + cedula +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", direccion='" + direccion + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", fecha_inscripcion=" + fecha_inscripcion +
                ", celular=" + celular +
                ", tel_habitacion=" + tel_habitacion +
                ", password='" + password + '\'' +
                '}';
    }
}
