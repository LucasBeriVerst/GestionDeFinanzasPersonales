package com.finanzasapp.backend.service.interfaces;

import com.finanzasapp.backend.model.dto.UsuarioDTO;

public interface IAutenticacionService {
    UsuarioDTO login(String username, String password);
    void logout();
    UsuarioDTO getUsuarioActual();
    boolean isUsuarioLogueado();
}
