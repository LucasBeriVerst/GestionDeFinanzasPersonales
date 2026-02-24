package com.finanzasapp.backend.model.dto;

import com.finanzasapp.backend.model.enums.TipoCategoria;

public class CategoriaGastoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String icono;
    private TipoCategoria tipo;
    private Boolean activo;

    public CategoriaGastoDTO() {
    }

    public CategoriaGastoDTO(Long id, String nombre, String descripcion, String icono, TipoCategoria tipo, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
        this.tipo = tipo;
        this.activo = activo;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
