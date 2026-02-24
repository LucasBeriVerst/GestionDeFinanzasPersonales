package com.finanzasapp.model.entity;

import com.finanzasapp.model.enums.TipoCategoria;
import jakarta.persistence.*;

@Entity
@Table(name = "categorias_gasto")
public class CategoriaGasto extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @Column(length = 50)
    private String icono;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private TipoCategoria tipo;

    public CategoriaGasto() {
    }

    public CategoriaGasto(String nombre, String descripcion, String icono, TipoCategoria tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.tipo = tipo;
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

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public TipoCategoria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCategoria tipo) {
        this.tipo = tipo;
    }
}
