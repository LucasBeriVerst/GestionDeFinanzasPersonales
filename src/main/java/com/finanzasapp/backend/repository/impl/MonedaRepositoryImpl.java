package com.finanzasapp.repository.impl;

import com.finanzasapp.model.entity.Moneda;
import com.finanzasapp.repository.MonedaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class MonedaRepositoryImpl implements MonedaRepository {

    private final EntityManager em;

    public MonedaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Moneda save(Moneda moneda) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (moneda.getId() == null) {
                em.persist(moneda);
            } else {
                moneda = em.merge(moneda);
            }
            tx.commit();
            return moneda;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar moneda", e);
        }
    }

    @Override
    public Optional<Moneda> findById(Long id) {
        return Optional.ofNullable(em.find(Moneda.class, id));
    }

    @Override
    public Optional<Moneda> findByCodigoISO(String codigoISO) {
        try {
            Moneda moneda = em.createQuery(
                "SELECT m FROM Moneda m WHERE m.codigoISO = :codigoISO", Moneda.class)
                .setParameter("codigoISO", codigoISO)
                .getSingleResult();
            return Optional.of(moneda);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Moneda> findByPredeterminadaTrue() {
        try {
            Moneda moneda = em.createQuery(
                "SELECT m FROM Moneda m WHERE m.predeterminada = true", Moneda.class)
                .getSingleResult();
            return Optional.of(moneda);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Moneda> findAll() {
        return em.createQuery("SELECT m FROM Moneda m", Moneda.class).getResultList();
    }

    @Override
    public List<Moneda> findByActivoTrue() {
        return em.createQuery("SELECT m FROM Moneda m WHERE m.activa = true", Moneda.class).getResultList();
    }

    @Override
    public void delete(Moneda moneda) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(moneda) ? moneda : em.merge(moneda));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar moneda", e);
        }
    }
}
