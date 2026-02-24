package com.finanzasapp.repository;

import com.finanzasapp.model.entity.CategoriaGasto;
import java.util.List;
import java.util.Optional;

public interface CategoriaGastoRepository {
    CategoriaGasto save(CategoriaGasto categoria);
    Optional<CategoriaGasto> findById(Long id);
    Optional<CategoriaGasto> findByNombre(String nombre);
    List<CategoriaGasto> findAll();
    List<CategoriaGasto> findByActivoTrue();
    void delete(CategoriaGasto categoria);
}
