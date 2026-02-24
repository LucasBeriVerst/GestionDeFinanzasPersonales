package com.finanzasapp.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tipos_cuenta")
public class TipoCuenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @Column(name = "permite_depositos", nullable = false)
    private Boolean permiteDepositos = true;

    @Column(name = "permite_retiros", nullable = false)
    private Boolean permiteRetiros = true;

    @Column(nullable = false)
    private Boolean activo = true;

    public TipoCuenta() {
    }

    public TipoCuenta(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.permiteDepositos = true;
        this.permiteRetiros = true;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getPermiteDepositos() {
        return permiteDepositos;
    }

    public void setPermiteDepositos(Boolean permiteDepositos) {
        this.permiteDepositos = permiteDepositos;
    }

    public Boolean getPermiteRetiros() {
        return permiteRetiros;
    }

    public void setPermiteRetiros(Boolean permiteRetiros) {
        this.permiteRetiros = permiteRetiros;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
