package com.finanzasapp.repository.impl;

import com.finanzasapp.model.entity.Gasto;
import com.finanzasapp.repository.GastoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class GastoRepositoryImpl implements GastoRepository {

    private final EntityManager em;

    public GastoRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Gasto save(Gasto gasto) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (gasto.getId() == null) {
                em.persist(gasto);
            } else {
                gasto = em.merge(gasto);
            }
            tx.commit();
            return gasto;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar gasto", e);
        }
    }

    @Override
    public Optional<Gasto> findById(Long id) {
        return Optional.ofNullable(em.find(Gasto.class, id));
    }

    @Override
    public List<Gasto> findByUsuarioId(Long usuarioId) {
        return em.createQuery(
            "SELECT g FROM Gasto g WHERE g.usuario.id = :usuarioId", Gasto.class)
            .setParameter("usuarioId", usuarioId)
            .getResultList();
    }

    @Override
    public List<Gasto> findByCuentaId(Long cuentaId) {
        return em.createQuery(
            "SELECT g FROM Gasto g WHERE g.cuenta.id = :cuentaId", Gasto.class)
            .setParameter("cuentaId", cuentaId)
            .getResultList();
    }

    @Override
    public List<Gasto> findByUsuarioIdAndActivoTrue(Long usuarioId) {
        return em.createQuery(
            "SELECT g FROM Gasto g WHERE g.usuario.id = :usuarioId AND g.activo = true", Gasto.class)
            .setParameter("usuarioId", usuarioId)
            .getResultList();
    }

    @Override
    public List<Gasto> findByCuentaIdAndActivoTrue(Long cuentaId) {
        return em.createQuery(
            "SELECT g FROM Gasto g WHERE g.cuenta.id = :cuentaId AND g.activo = true", Gasto.class)
            .setParameter("cuentaId", cuentaId)
            .getResultList();
    }

    @Override
    public List<Gasto> findAll() {
        return em.createQuery("SELECT g FROM Gasto g", Gasto.class).getResultList();
    }

    @Override
    public List<Gasto> findByActivoTrue() {
        return em.createQuery("SELECT g FROM Gasto g WHERE g.activo = true", Gasto.class).getResultList();
    }

    @Override
    public void delete(Gasto gasto) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(gasto) ? gasto : em.merge(gasto));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar gasto", e);
        }
    }
}
