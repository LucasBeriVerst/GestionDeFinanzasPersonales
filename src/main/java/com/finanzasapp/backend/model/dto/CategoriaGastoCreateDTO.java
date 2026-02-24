package com.finanzasapp.backend.model.dto;

import com.finanzasapp.backend.model.enums.TipoCategoria;

public class CategoriaGastoCreateDTO {
    private String nombre;
    private String descripcion;
    private String icono;
    private TipoCategoria tipo;

    public CategoriaGastoCreateDTO() {
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
