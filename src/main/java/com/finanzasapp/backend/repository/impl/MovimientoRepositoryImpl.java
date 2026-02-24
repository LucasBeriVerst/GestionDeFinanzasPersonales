package com.finanzasapp.backend.repository.impl;

import com.finanzasapp.backend.model.entity.Movimiento;
import com.finanzasapp.backend.repository.MovimientoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class MovimientoRepositoryImpl implements MovimientoRepository {

    private final EntityManager em;

    public MovimientoRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Movimiento save(Movimiento movimiento) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (movimiento.getId() == null) {
                em.persist(movimiento);
            } else {
                movimiento = em.merge(movimiento);
            }
            tx.commit();
            return movimiento;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar movimiento", e);
        }
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        return Optional.ofNullable(em.find(Movimiento.class, id));
    }

    @Override
    public List<Movimiento> findByCuentaId(Long cuentaId) {
        return em.createQuery(
            "SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId", Movimiento.class)
            .setParameter("cuentaId", cuentaId)
            .getResultList();
    }

    @Override
    public List<Movimiento> findByCuentaIdAndActivoTrue(Long cuentaId) {
        return em.createQuery(
            "SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND m.activo = true", Movimiento.class)
            .setParameter("cuentaId", cuentaId)
            .getResultList();
    }

    @Override
    public List<Movimiento> findAll() {
        return em.createQuery("SELECT m FROM Movimiento m", Movimiento.class).getResultList();
    }

    @Override
    public List<Movimiento> findByActivoTrue() {
        return em.createQuery("SELECT m FROM Movimiento m WHERE m.activo = true", Movimiento.class).getResultList();
    }

    @Override
    public void delete(Movimiento movimiento) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(movimiento) ? movimiento : em.merge(movimiento));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar movimiento", e);
        }
    }
}
