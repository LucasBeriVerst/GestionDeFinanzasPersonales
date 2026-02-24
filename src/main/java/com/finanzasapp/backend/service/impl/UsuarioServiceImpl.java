package com.finanzasapp.backend.service.impl;

import com.finanzasapp.backend.exception.ValidationException;
import com.finanzasapp.backend.model.dto.UsuarioCreateDTO;
import com.finanzasapp.backend.model.dto.UsuarioDTO;
import com.finanzasapp.backend.model.entity.Usuario;
import com.finanzasapp.backend.repository.UsuarioRepository;
import com.finanzasapp.backend.security.EncriptadorContrasena;
import com.finanzasapp.backend.service.interfaces.IUsuarioService;
import com.finanzasapp.backend.validator.UsuarioValidator;
import com.finanzasapp.backend.validator.ValidadorUtil;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDTO crear(UsuarioCreateDTO dto) {
        List<String> errores = UsuarioValidator.validarCreacion(dto);
        
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            errores.add("El username ya existe");
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(EncriptadorContrasena.hash(dto.getPassword()));
        usuario.setEmail(dto.getEmail());

        usuario = usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            usuario.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(EncriptadorContrasena.hash(dto.getPassword()));
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }

        usuario = usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return toDTO(usuario);
    }

    @Override
    public UsuarioDTO buscarPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return toDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<UsuarioDTO> listarActivos() {
        return usuarioRepository.findByActivoTrue().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.softDelete();
        usuarioRepository.save(usuario);
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
