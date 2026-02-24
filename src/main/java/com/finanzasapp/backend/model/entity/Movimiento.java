package com.finanzasapp.backend.model.entity;

import com.finanzasapp.backend.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
public class Movimiento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private CuentaFinanciera cuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 10)
    private TipoMovimiento tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 200)
    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gasto")
    private Gasto gasto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inversion")
    private Inversion inversion;

    public Movimiento() {
    }

    public Movimiento(CuentaFinanciera cuenta, TipoMovimiento tipo, BigDecimal monto, String referencia) {
        this.cuenta = cuenta;
        this.tipo = tipo;
        this.monto = monto;
        this.referencia = referencia;
        this.fecha = LocalDateTime.now();
    }

    public CuentaFinanciera getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaFinanciera cuenta) {
        this.cuenta = cuenta;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Gasto getGasto() {
        return gasto;
    }

    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    public Inversion getInversion() {
        return inversion;
    }

    public void setInversion(Inversion inversion) {
        this.inversion = inversion;
    }
}
