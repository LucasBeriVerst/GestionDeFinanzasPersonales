package com.finanzasapp.backend.model.entity;

import com.finanzasapp.backend.model.enums.NivelRiesgo;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "categorias_inversion")
public class CategoriaInversion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_riesgo", length = 15)
    private NivelRiesgo nivelRiesgo;

    @Column(name = "rendimiento_promedio", precision = 5, scale = 2)
    private BigDecimal rendimientoPromedio;

    public CategoriaInversion() {
    }

    public CategoriaInversion(String nombre, String descripcion, NivelRiesgo nivelRiesgo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelRiesgo = nivelRiesgo;
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

    public NivelRiesgo getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(NivelRiesgo nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public BigDecimal getRendimientoPromedio() {
        return rendimientoPromedio;
    }

    public void setRendimientoPromedio(BigDecimal rendimientoPromedio) {
        this.rendimientoPromedio = rendimientoPromedio;
    }
}
