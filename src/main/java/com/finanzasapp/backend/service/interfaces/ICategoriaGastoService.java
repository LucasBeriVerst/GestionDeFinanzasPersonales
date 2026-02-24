package com.finanzasapp.service.interfaces;

import com.finanzasapp.model.dto.CategoriaGastoCreateDTO;
import com.finanzasapp.model.dto.CategoriaGastoDTO;
import java.util.List;

public interface ICategoriaGastoService {
    CategoriaGastoDTO crear(CategoriaGastoCreateDTO dto);
    CategoriaGastoDTO actualizar(Long id, CategoriaGastoCreateDTO dto);
    CategoriaGastoDTO buscarPorId(Long id);
    List<CategoriaGastoDTO> listarTodos();
    List<CategoriaGastoDTO> listarActivos();
    void eliminar(Long id);
}
