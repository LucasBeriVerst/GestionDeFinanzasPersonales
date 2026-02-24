package com.finanzasapp.controller;

import com.finanzasapp.model.dto.GastoCreateDTO;
import com.finanzasapp.model.dto.GastoDTO;
import com.finanzasapp.service.interfaces.IGastoService;
import java.math.BigDecimal;
import java.util.List;

public class GastoController {

    private final IGastoService gastoService;

    public GastoController(IGastoService gastoService) {
        this.gastoService = gastoService;
    }

    public GastoDTO crear(Long idUsuario, Long idCuenta, Long idCategoria, 
                         BigDecimal monto, String descripcion, Long idMoneda) {
        try {
            GastoCreateDTO dto = new GastoCreateDTO();
            dto.setIdUsuario(idUsuario);
            dto.setIdCuenta(idCuenta);
            dto.setIdCategoria(idCategoria);
            dto.setMonto(monto);
            dto.setDescripcion(descripcion);
            dto.setIdMoneda(idMoneda);
            return gastoService.crear(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public GastoDTO actualizar(Long id, Long idCategoria, BigDecimal monto, String descripcion) {
        try {
            GastoCreateDTO dto = new GastoCreateDTO();
            dto.setIdCategoria(idCategoria);
            dto.setMonto(monto);
            dto.setDescripcion(descripcion);
            return gastoService.actualizar(id, dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public GastoDTO buscarPorId(Long id) {
        return gastoService.buscarPorId(id);
    }

    public List<GastoDTO> listarPorUsuario(Long usuarioId) {
        return gastoService.listarPorUsuario(usuarioId);
    }

    public List<GastoDTO> listarPorCuenta(Long cuentaId) {
        return gastoService.listarPorCuenta(cuentaId);
    }

    public List<GastoDTO> listarTodos() {
        return gastoService.listarTodos();
    }

    public List<GastoDTO> listarActivos() {
        return gastoService.listarActivos();
    }

    public void eliminar(Long id) {
        gastoService.eliminar(id);
    }
}
