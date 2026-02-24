package com.finanzasapp.service.interfaces;

import com.finanzasapp.model.dto.UsuarioDTO;

public interface IAutenticacionService {
    UsuarioDTO login(String username, String password);
    void logout();
    UsuarioDTO getUsuarioActual();
    boolean isUsuarioLogueado();
}
