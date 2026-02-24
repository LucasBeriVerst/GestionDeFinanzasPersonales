package com.finanzasapp.repository;

import com.finanzasapp.model.entity.Movimiento;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository {
    Movimiento save(Movimiento movimiento);
    Optional<Movimiento> findById(Long id);
    List<Movimiento> findByCuentaId(Long cuentaId);
    List<Movimiento> findByCuentaIdAndActivoTrue(Long cuentaId);
    List<Movimiento> findAll();
    List<Movimiento> findByActivoTrue();
    void delete(Movimiento movimiento);
}
