package com.finanzasapp.backend.security;

import com.finanzasapp.backend.model.entity.Usuario;

public class SesionUsuario {

    private static SesionUsuario instancia;
    private Usuario usuarioActual;
    private boolean logueado;

    private SesionUsuario() {
    }

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            synchronized (SesionUsuario.class) {
                if (instancia == null) {
                    instancia = new SesionUsuario();
                }
            }
        }
        return instancia;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
        this.logueado = true;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        this.logueado = false;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Long getUsuarioId() {
        return usuarioActual != null ? usuarioActual.getId() : null;
    }

    public String getUsername() {
        return usuarioActual != null ? usuarioActual.getUsername() : null;
    }

    public boolean isLogueado() {
        return logueado && usuarioActual != null;
    }
}
