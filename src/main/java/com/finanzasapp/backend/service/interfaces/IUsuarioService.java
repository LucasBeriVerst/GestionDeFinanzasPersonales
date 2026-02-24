package com.finanzasapp.backend.service.interfaces;

import com.finanzasapp.backend.model.dto.UsuarioCreateDTO;
import com.finanzasapp.backend.model.dto.UsuarioDTO;
import java.util.List;

public interface IUsuarioService {
    UsuarioDTO crear(UsuarioCreateDTO dto);
    UsuarioDTO actualizar(Long id, UsuarioCreateDTO dto);
    UsuarioDTO buscarPorId(Long id);
    UsuarioDTO buscarPorUsername(String username);
    List<UsuarioDTO> listarTodos();
    List<UsuarioDTO> listarActivos();
    void eliminar(Long id);
}
