package com.finanzasapp.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "monedas")
public class Moneda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_iso", unique = true, nullable = false, length = 3)
    private String codigoISO;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 5)
    private String simbolo;

    @Column(name = "tipo_cambio_base", precision = 10, scale = 4)
    private BigDecimal tipoCambioBase;

    @Column(nullable = false)
    private Boolean activa = true;

    @Column(name = "predeterminada", nullable = false)
    private Boolean predeterminada = false;

    public Moneda() {
    }

    public Moneda(String codigoISO, String nombre, String simbolo) {
        this.codigoISO = codigoISO;
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.activa = true;
        this.predeterminada = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoISO() {
        return codigoISO;
    }

    public void setCodigoISO(String codigoISO) {
        this.codigoISO = codigoISO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public BigDecimal getTipoCambioBase() {
        return tipoCambioBase;
    }

    public void setTipoCambioBase(BigDecimal tipoCambioBase) {
        this.tipoCambioBase = tipoCambioBase;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Boolean getPredeterminada() {
        return predeterminada;
    }

    public void setPredeterminada(Boolean predeterminada) {
        this.predeterminada = predeterminada;
    }
}
