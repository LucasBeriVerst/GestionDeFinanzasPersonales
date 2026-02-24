package com.finanzasapp.backend.repository.impl;

import com.finanzasapp.backend.model.entity.CuentaFinanciera;
import com.finanzasapp.backend.repository.CuentaFinancieraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class CuentaFinancieraRepositoryImpl implements CuentaFinancieraRepository {

    private final EntityManager em;

    public CuentaFinancieraRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public CuentaFinanciera save(CuentaFinanciera cuenta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (cuenta.getId() == null) {
                em.persist(cuenta);
            } else {
                cuenta = em.merge(cuenta);
            }
            tx.commit();
            return cuenta;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar cuenta", e);
        }
    }

    @Override
    public Optional<CuentaFinanciera> findById(Long id) {
        return Optional.ofNullable(em.find(CuentaFinanciera.class, id));
    }

    @Override
    public List<CuentaFinanciera> findByUsuarioId(Long usuarioId) {
        return em.createQuery(
            "SELECT c FROM CuentaFinanciera c WHERE c.usuario.id = :usuarioId", CuentaFinanciera.class)
            .setParameter("usuarioId", usuarioId)
            .getResultList();
    }

    @Override
    public List<CuentaFinanciera> findByUsuarioIdAndActivoTrue(Long usuarioId) {
        return em.createQuery(
            "SELECT c FROM CuentaFinanciera c WHERE c.usuario.id = :usuarioId AND c.activo = true", 
            CuentaFinanciera.class)
            .setParameter("usuarioId", usuarioId)
            .getResultList();
    }

    @Override
    public List<CuentaFinanciera> findAll() {
        return em.createQuery("SELECT c FROM CuentaFinanciera c", CuentaFinanciera.class).getResultList();
    }

    @Override
    public List<CuentaFinanciera> findByActivoTrue() {
        return em.createQuery("SELECT c FROM CuentaFinanciera c WHERE c.activo = true", CuentaFinanciera.class).getResultList();
    }

    @Override
    public void delete(CuentaFinanciera cuenta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(cuenta) ? cuenta : em.merge(cuenta));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar cuenta", e);
        }
    }
}
