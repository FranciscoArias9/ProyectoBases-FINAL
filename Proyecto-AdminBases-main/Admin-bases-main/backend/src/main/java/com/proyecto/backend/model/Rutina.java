package com.proyecto.backend.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rutinas")
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_rutina;

    @ManyToOne
    @JoinColumn(name = "cliente", referencedColumnName = "cedula")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "instructor", referencedColumnName = "cedula")
    private Empleado instructor;

    @ManyToOne
    @JoinColumn(name = "maquina", referencedColumnName = "id_maquina")
    private Maquina maquina;

    private Date fecha;
    private int horas;
    private String nombre;

    // Getters y Setters
    public int getId_rutina() {
        return id_rutina;
    }

    public void setId_rutina(int id_rutina) {
        this.id_rutina = id_rutina;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getInstructor() {
        return instructor;
    }

    public void setInstructor(Empleado instructor) {
        this.instructor = instructor;
    }

    public Maquina getMaquina() {
        return maquina;
    }

    public void setMaquina(Maquina maquina) {
        this.maquina = maquina;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
