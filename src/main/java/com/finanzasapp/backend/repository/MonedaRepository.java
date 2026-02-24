package com.finanzasapp.backend.repository;

import com.finanzasapp.backend.model.entity.Moneda;
import java.util.List;
import java.util.Optional;

public interface MonedaRepository {
    Moneda save(Moneda moneda);
    Optional<Moneda> findById(Long id);
    Optional<Moneda> findByCodigoISO(String codigoISO);
    Optional<Moneda> findByPredeterminadaTrue();
    List<Moneda> findAll();
    List<Moneda> findByActivoTrue();
    void delete(Moneda moneda);
}
