package com.finanzasapp.service.interfaces;

import com.finanzasapp.model.dto.GastoCreateDTO;
import com.finanzasapp.model.dto.GastoDTO;
import java.util.List;

public interface IGastoService {
    GastoDTO crear(GastoCreateDTO dto);
    GastoDTO actualizar(Long id, GastoCreateDTO dto);
    GastoDTO buscarPorId(Long id);
    List<GastoDTO> listarPorUsuario(Long usuarioId);
    List<GastoDTO> listarPorCuenta(Long cuentaId);
    List<GastoDTO> listarTodos();
    List<GastoDTO> listarActivos();
    void eliminar(Long id);
}
