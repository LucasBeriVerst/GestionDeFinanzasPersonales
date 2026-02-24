package com.finanzasapp.backend.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gastos")
public class Gasto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private CuentaFinanciera cuenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaGasto categoria;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_moneda")
    private Moneda moneda;

    public Gasto() {
    }

    public Gasto(Usuario usuario, CuentaFinanciera cuenta, CategoriaGasto categoria, 
                 BigDecimal monto, String descripcion) {
        this.usuario = usuario;
        this.cuenta = cuenta;
        this.categoria = categoria;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CuentaFinanciera getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaFinanciera cuenta) {
        this.cuenta = cuenta;
    }

    public CategoriaGasto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaGasto categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
}
