package com.finanzasapp.backend.repository.impl;

import com.finanzasapp.backend.model.entity.TipoCuenta;
import com.finanzasapp.backend.repository.TipoCuentaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class TipoCuentaRepositoryImpl implements TipoCuentaRepository {

    private final EntityManager em;

    public TipoCuentaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public TipoCuenta save(TipoCuenta tipoCuenta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (tipoCuenta.getId() == null) {
                em.persist(tipoCuenta);
            } else {
                tipoCuenta = em.merge(tipoCuenta);
            }
            tx.commit();
            return tipoCuenta;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar tipo cuenta", e);
        }
    }

    @Override
    public Optional<TipoCuenta> findById(Long id) {
        return Optional.ofNullable(em.find(TipoCuenta.class, id));
    }

    @Override
    public Optional<TipoCuenta> findByNombre(String nombre) {
        try {
            TipoCuenta tipoCuenta = em.createQuery(
                "SELECT t FROM TipoCuenta t WHERE t.nombre = :nombre", TipoCuenta.class)
                .setParameter("nombre", nombre)
                .getSingleResult();
            return Optional.of(tipoCuenta);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TipoCuenta> findAll() {
        return em.createQuery("SELECT t FROM TipoCuenta t", TipoCuenta.class).getResultList();
    }

    @Override
    public List<TipoCuenta> findByActivoTrue() {
        return em.createQuery("SELECT t FROM TipoCuenta t WHERE t.activo = true", TipoCuenta.class).getResultList();
    }

    @Override
    public void delete(TipoCuenta tipoCuenta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(tipoCuenta) ? tipoCuenta : em.merge(tipoCuenta));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar tipo cuenta", e);
        }
    }
}
