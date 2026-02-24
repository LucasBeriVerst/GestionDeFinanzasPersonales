package com.finanzasapp.repository.impl;

import com.finanzasapp.model.entity.Usuario;
import com.finanzasapp.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final EntityManager em;

    public UsuarioRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Usuario save(Usuario usuario) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                usuario = em.merge(usuario);
            }
            tx.commit();
            return usuario;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(em.find(Usuario.class, id));
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        try {
            Usuario usuario = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class)
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(usuario);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> findByUsernameAndActivoTrue(String username) {
        try {
            Usuario usuario = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :username AND u.activo = true", Usuario.class)
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(usuario);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Usuario> findAll() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Override
    public List<Usuario> findByActivoTrue() {
        return em.createQuery("SELECT u FROM Usuario u WHERE u.activo = true", Usuario.class).getResultList();
    }

    @Override
    public void delete(Usuario usuario) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(usuario) ? usuario : em.merge(usuario));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }
}
