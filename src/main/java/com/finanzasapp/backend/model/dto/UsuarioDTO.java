package com.finanzasapp.backend.model.dto;

import java.time.LocalDateTime;

public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime fechaRegistro;
    private Boolean activo;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String username, String email, LocalDateTime fechaRegistro, Boolean activo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
