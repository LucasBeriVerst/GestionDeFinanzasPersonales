package com.finanzasapp.repository;

import com.finanzasapp.model.entity.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByUsernameAndActivoTrue(String username);
    java.util.List<Usuario> findAll();
    java.util.List<Usuario> findByActivoTrue();
    void delete(Usuario usuario);
}
