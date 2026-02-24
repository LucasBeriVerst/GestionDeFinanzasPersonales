package com.finanzasapp.service.interfaces;

import com.finanzasapp.model.dto.CuentaFinancieraCreateDTO;
import com.finanzasapp.model.dto.CuentaFinancieraDTO;
import java.util.List;

public interface ICuentaFinancieraService {
    CuentaFinancieraDTO crear(CuentaFinancieraCreateDTO dto);
    CuentaFinancieraDTO actualizar(Long id, CuentaFinancieraCreateDTO dto);
    CuentaFinancieraDTO buscarPorId(Long id);
    List<CuentaFinancieraDTO> listarPorUsuario(Long usuarioId);
    List<CuentaFinancieraDTO> listarTodos();
    List<CuentaFinancieraDTO> listarActivos();
    void eliminar(Long id);
}
