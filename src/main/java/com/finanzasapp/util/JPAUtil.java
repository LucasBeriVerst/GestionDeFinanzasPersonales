package com.finanzasapp.util;

import com.finanzasapp.database.JPAConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilitario centralizado para manejo de transacciones JPA.
 * 
 * Provee métodos estáticos para:
 * - Iniciar transacciones
 * - Confirmar cambios (commit)
 * - Revertir cambios (rollback)
 * - Cerrar EntityManager correctamente
 * 
 * Uso típico:
 * 
 *   EntityManager em = null;
 *   try {
 *       em = JPAUtil.getEntityManager();
 *       JPAUtil.beginTransaction(em);
 *       
 *       // Operaciones...
 *       
 *       JPAUtil.commit(em);
 *   } catch (Exception e) {
 *       JPAUtil.rollback(em);
 *       throw e;
 *   } finally {
 *       JPAUtil.close(em);
 *   }
 * 
 * @author FinanzasApp
 * @version 1.0
 */
public final class JPAUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JPAUtil.class);

    // Constructor privado - previene instanciación
    private JPAUtil() {
        throw new UnsupportedOperationException("Clase utilitaria - no instanciar");
    }

    /**
     * Obtiene un EntityManager nuevo.
     * 
     * @return EntityManager configurado
     */
    public static EntityManager getEntityManager() {
        return JPAConfig.getEntityManager();
    }

    /**
     * Inicia una transacción en el EntityManager.
     * 
     * @param em EntityManager con transacción activa
     * @throws IllegalStateException si no puede iniciar transacción
     */
    public static void beginTransaction(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager no puede ser null");
        }
        
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
            LOG.debug("Transacción iniciada");
        } else {
            LOG.warn("Ya existe una transacción activa");
        }
    }

    /**
     * Confirma (commit) la transacción actual.
     * 
     * @param em EntityManager con transacción activa
     * @throws IllegalStateException si no hay transacción activa
     */
    public static void commit(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager no puede ser null");
        }
        
        EntityTransaction tx = em.getTransaction();
        if (tx != null && tx.isActive()) {
            try {
                tx.commit();
                LOG.debug("Transacción confirmada (commit)");
            } catch (Exception e) {
                LOG.error("Error en commit: {}", e.getMessage());
                throw e;
            }
        } else {
            LOG.warn("No hay transacción activa para confirmar");
        }
    }

    /**
     * Revierte (rollback) la transacción actual.
     * 
     * @param em EntityManager con transacción activa
     */
    public static void rollback(EntityManager em) {
        if (em == null) {
            return;
        }
        
        EntityTransaction tx = em.getTransaction();
        if (tx != null && tx.isActive()) {
            try {
                tx.rollback();
                LOG.debug("Transacción revertida (rollback)");
            } catch (Exception e) {
                LOG.error("Error en rollback: {}", e.getMessage());
            }
        }
    }

    /**
     * Cierra el EntityManager correctamente.
     * 
     * @param em EntityManager a cerrar
     */
    public static void close(EntityManager em) {
        if (em != null && em.isOpen()) {
            try {
                em.close();
                LOG.debug("EntityManager cerrado");
            } catch (Exception e) {
                LOG.error("Error al cerrar EntityManager: {}", e.getMessage());
            }
        }
    }

    /**
     * Método combinado: ejecuta una operación en una transacción.
     * Simplifica el código de uso.
     * 
     * @param operation Operación a ejecutar (recibe EntityManager)
     * @return Resultado de la operación
     * @throws Exception si falla la operación
     */
    public static <T> T executeInTransaction(TransactionOperation<T> operation) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            beginTransaction(em);
            
            T result = operation.execute(em);
            
            commit(em);
            return result;
            
        } catch (Exception e) {
            rollback(em);
            throw e;
        } finally {
            close(em);
        }
    }

    /**
     * Versión void de executeInTransaction.
     * 
     * @param operation Operación a ejecutar
     * @throws Exception si falla la operación
     */
    public static void executeInTransactionVoid(TransactionVoidOperation operation) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            beginTransaction(em);
            
            operation.execute(em);
            
            commit(em);
            
        } catch (Exception e) {
            rollback(em);
            throw e;
        } finally {
            close(em);
        }
    }

    /**
     * Verifica si hay una transacción activa.
     * 
     * @param em EntityManager a verificar
     * @return true si hay transacción activa
     */
    public static boolean isTransactionActive(EntityManager em) {
        return em != null && em.getTransaction() != null && em.getTransaction().isActive();
    }

    /**
     * Interface funcional para operaciones con transacción.
     */
    @FunctionalInterface
    public interface TransactionOperation<T> {
        T execute(EntityManager em) throws Exception;
    }

    /**
     * Interface funcional para operaciones void con transacción.
     */
    @FunctionalInterface
    public interface TransactionVoidOperation {
        void execute(EntityManager em) throws Exception;
    }

}
