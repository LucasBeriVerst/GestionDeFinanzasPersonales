package com.finanzasapp.backend.controller;

import com.finanzasapp.backend.model.dto.CuentaFinancieraCreateDTO;
import com.finanzasapp.backend.model.dto.CuentaFinancieraDTO;
import com.finanzasapp.backend.service.interfaces.ICuentaFinancieraService;
import java.util.List;

public class CuentaFinancieraController {

    private final ICuentaFinancieraService cuentaService;

    public CuentaFinancieraController(ICuentaFinancieraService cuentaService) {
        this.cuentaService = cuentaService;
    }

    public CuentaFinancieraDTO crear(String nombre, Long idUsuario, Long idTipoCuenta, Long idMoneda) {
        try {
            CuentaFinancieraCreateDTO dto = new CuentaFinancieraCreateDTO();
            dto.setNombre(nombre);
            dto.setIdUsuario(idUsuario);
            dto.setIdTipoCuenta(idTipoCuenta);
            dto.setIdMoneda(idMoneda);
            return cuentaService.crear(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CuentaFinancieraDTO actualizar(Long id, String nombre, Long idTipoCuenta, Long idMoneda) {
        try {
            CuentaFinancieraCreateDTO dto = new CuentaFinancieraCreateDTO();
            dto.setNombre(nombre);
            dto.setIdTipoCuenta(idTipoCuenta);
            dto.setIdMoneda(idMoneda);
            return cuentaService.actualizar(id, dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CuentaFinancieraDTO buscarPorId(Long id) {
        return cuentaService.buscarPorId(id);
    }

    public List<CuentaFinancieraDTO> listarPorUsuario(Long usuarioId) {
        return cuentaService.listarPorUsuario(usuarioId);
    }

    public List<CuentaFinancieraDTO> listarTodos() {
        return cuentaService.listarTodos();
    }

    public List<CuentaFinancieraDTO> listarActivos() {
        return cuentaService.listarActivos();
    }

    public void eliminar(Long id) {
        cuentaService.eliminar(id);
    }
}
