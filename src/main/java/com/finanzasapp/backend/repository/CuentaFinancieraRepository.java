package com.finanzasapp.backend.repository;

import com.finanzasapp.backend.model.entity.CuentaFinanciera;
import java.util.List;
import java.util.Optional;

public interface CuentaFinancieraRepository {
    CuentaFinanciera save(CuentaFinanciera cuenta);
    Optional<CuentaFinanciera> findById(Long id);
    List<CuentaFinanciera> findByUsuarioId(Long usuarioId);
    List<CuentaFinanciera> findByUsuarioIdAndActivoTrue(Long usuarioId);
    List<CuentaFinanciera> findAll();
    List<CuentaFinanciera> findByActivoTrue();
    void delete(CuentaFinanciera cuenta);
}
