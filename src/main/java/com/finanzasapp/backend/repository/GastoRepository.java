package com.finanzasapp.repository;

import com.finanzasapp.model.entity.Gasto;
import java.util.List;
import java.util.Optional;

public interface GastoRepository {
    Gasto save(Gasto gasto);
    Optional<Gasto> findById(Long id);
    List<Gasto> findByUsuarioId(Long usuarioId);
    List<Gasto> findByCuentaId(Long cuentaId);
    List<Gasto> findByUsuarioIdAndActivoTrue(Long usuarioId);
    List<Gasto> findByCuentaIdAndActivoTrue(Long cuentaId);
    List<Gasto> findAll();
    List<Gasto> findByActivoTrue();
    void delete(Gasto gasto);
}
