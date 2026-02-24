package com.finanzasapp.backend.model.dto;

import java.math.BigDecimal;

public class CuentaFinancieraCreateDTO {
    private String nombre;
    private Long idUsuario;
    private Long idTipoCuenta;
    private Long idMoneda;
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    public CuentaFinancieraCreateDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public void setIdTipoCuenta(Long idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public Long getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(Long idMoneda) {
        this.idMoneda = idMoneda;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
    }
}
