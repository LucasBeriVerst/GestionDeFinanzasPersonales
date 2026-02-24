package com.finanzasapp.backend.service.impl;

import com.finanzasapp.backend.model.dto.UsuarioDTO;
import com.finanzasapp.backend.model.entity.Usuario;
import com.finanzasapp.backend.repository.UsuarioRepository;
import com.finanzasapp.backend.security.EncriptadorContrasena;
import com.finanzasapp.backend.security.SesionUsuario;
import com.finanzasapp.backend.service.interfaces.IAutenticacionService;

public class AutenticacionServiceImpl implements IAutenticacionService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacionServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDTO login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("El password es obligatorio");
        }

        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
            .orElseThrow(() -> new IllegalArgumentException("Usuario o password incorrectos"));

        if (!EncriptadorContrasena.verificar(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Usuario o password incorrectos");
        }

        SesionUsuario.getInstancia().iniciarSesion(usuario);
        return toDTO(usuario);
    }

    @Override
    public void logout() {
        SesionUsuario.getInstancia().cerrarSesion();
    }

    @Override
    public UsuarioDTO getUsuarioActual() {
        SesionUsuario sesion = SesionUsuario.getInstancia();
        if (!sesion.isLogueado()) {
            return null;
        }
        return toDTO(sesion.getUsuarioActual());
    }

    @Override
    public boolean isUsuarioLogueado() {
        return SesionUsuario.getInstancia().isLogueado();
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getFechaCreacion(),
            usuario.getActivo()
        );
    }
}
