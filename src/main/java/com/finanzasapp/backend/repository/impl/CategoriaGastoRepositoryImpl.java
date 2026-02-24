package com.finanzasapp.repository.impl;

import com.finanzasapp.model.entity.CategoriaGasto;
import com.finanzasapp.repository.CategoriaGastoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class CategoriaGastoRepositoryImpl implements CategoriaGastoRepository {

    private final EntityManager em;

    public CategoriaGastoRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public CategoriaGasto save(CategoriaGasto categoria) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (categoria.getId() == null) {
                em.persist(categoria);
            } else {
                categoria = em.merge(categoria);
            }
            tx.commit();
            return categoria;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar categoría", e);
        }
    }

    @Override
    public Optional<CategoriaGasto> findById(Long id) {
        return Optional.ofNullable(em.find(CategoriaGasto.class, id));
    }

    @Override
    public Optional<CategoriaGasto> findByNombre(String nombre) {
        try {
            CategoriaGasto categoria = em.createQuery(
                "SELECT c FROM CategoriaGasto c WHERE c.nombre = :nombre", CategoriaGasto.class)
                .setParameter("nombre", nombre)
                .getSingleResult();
            return Optional.of(categoria);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CategoriaGasto> findAll() {
        return em.createQuery("SELECT c FROM CategoriaGasto c", CategoriaGasto.class).getResultList();
    }

    @Override
    public List<CategoriaGasto> findByActivoTrue() {
        return em.createQuery("SELECT c FROM CategoriaGasto c WHERE c.activo = true", CategoriaGasto.class).getResultList();
    }

    @Override
    public void delete(CategoriaGasto categoria) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(categoria) ? categoria : em.merge(categoria));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar categoría", e);
        }
    }
}
