package com.finanzasapp.controller;

import com.finanzasapp.model.dto.UsuarioDTO;
import com.finanzasapp.service.interfaces.IAutenticacionService;
import com.finanzasapp.service.interfaces.IUsuarioService;

public class LoginController {

    private final IAutenticacionService autenticacionService;
    private final IUsuarioService usuarioService;

    public LoginController(IAutenticacionService autenticacionService, IUsuarioService usuarioService) {
        this.autenticacionService = autenticacionService;
        this.usuarioService = usuarioService;
    }

    public UsuarioDTO login(String username, String password) {
        try {
            return autenticacionService.login(username, password);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void logout() {
        autenticacionService.logout();
    }

    public UsuarioDTO getUsuarioActual() {
        return autenticacionService.getUsuarioActual();
    }

    public boolean isLogueado() {
        return autenticacionService.isUsuarioLogueado();
    }

    public UsuarioDTO registrarUsuario(String username, String password, String email) {
        try {
            com.finanzasapp.model.dto.UsuarioCreateDTO dto = new com.finanzasapp.model.dto.UsuarioCreateDTO();
            dto.setUsername(username);
            dto.setPassword(password);
            dto.setEmail(email);
            return usuarioService.crear(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
