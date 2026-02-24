package com.finanzasapp.backend.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inversiones")
public class Inversion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta")
    private CuentaFinanciera cuenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaInversion categoria;

    @Column(name = "monto_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "valor_actual", precision = 15, scale = 2)
    private BigDecimal valorActual;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(precision = 5, scale = 2)
    private BigDecimal rentabilidad;

    @Column(nullable = false)
    private Boolean activa = true;

    @OneToMany(mappedBy = "inversion", cascade = CascadeType.ALL)
    private List<HistorialValor> historialValores = new ArrayList<>();

    public Inversion() {
    }

    public Inversion(Usuario usuario, CategoriaInversion categoria, BigDecimal montoInicial) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.montoInicial = montoInicial;
        this.valorActual = montoInicial;
        this.fechaInicio = LocalDateTime.now();
        this.activa = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CategoriaInversion getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaInversion categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(BigDecimal montoInicial) {
        this.montoInicial = montoInicial;
    }

    public BigDecimal getValorActual() {
        return valorActual;
    }

    public void setValorActual(BigDecimal valorActual) {
        this.valorActual = valorActual;
        calcularRentabilidad();
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getRentabilidad() {
        return rentabilidad;
    }

    public void setRentabilidad(BigDecimal rentabilidad) {
        this.rentabilidad = rentabilidad;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public List<HistorialValor> getHistorialValores() {
        return historialValores;
    }

    public void setHistorialValores(List<HistorialValor> historialValores) {
        this.historialValores = historialValores;
    }

    public void calcularRentabilidad() {
        if (montoInicial != null && montoInicial.compareTo(BigDecimal.ZERO) > 0 
            && valorActual != null) {
            this.rentabilidad = valorActual.subtract(montoInicial)
                .divide(montoInicial, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
        }
    }
}
