package com.finanzasapp.backend.repository;

import com.finanzasapp.backend.model.entity.TipoCuenta;
import java.util.List;
import java.util.Optional;

public interface TipoCuentaRepository {
    TipoCuenta save(TipoCuenta tipoCuenta);
    Optional<TipoCuenta> findById(Long id);
    Optional<TipoCuenta> findByNombre(String nombre);
    List<TipoCuenta> findAll();
    List<TipoCuenta> findByActivoTrue();
    void delete(TipoCuenta tipoCuenta);
}
