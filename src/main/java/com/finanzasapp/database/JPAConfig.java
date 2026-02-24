package com.finanzasapp.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clase de configuración centralizada para JPA/Hibernate.
 * 
 * Proveé singleton de EntityManagerFactory y Factory Methods
 * para crear EntityManagers thread-safe.
 * 
 * Uso:
 *   EntityManager em = JPAConfig.getEntityManager();
 * 
 * @author FinanzasApp
 * @version 1.0
 */
public final class JPAConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JPAConfig.class);
    private static final String PERSISTENCE_UNIT = "FinanzasAppPU";
    
    // Singleton de EntityManagerFactory
    private static volatile EntityManagerFactory entityManagerFactory;
    
    // Control de inicialización
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    
    // Constructor privado - previene instanciación
    private JPAConfig() {
        throw new UnsupportedOperationException("Clase de configuración - no instanciar");
    }

    /**
     * Obtiene el EntityManagerFactory singleton.
     * Thread-safe con inicialización lazy.
     * 
     * @return EntityManagerFactory configurado
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            synchronized (JPAConfig.class) {
                if (entityManagerFactory == null) {
                    LOG.info("Inicializando EntityManagerFactory...");
                    try {
                        // Cargar propiedades adicionales del sistema si existen
                        Map<String, Object> props = new HashMap<>();
                        
                        // Propiedades del sistema tienen prioridad
                        String dbUrl = System.getProperty("db.url");
                        String dbUser = System.getProperty("db.user");
                        String dbPassword = System.getProperty("db.password");
                        
                        if (dbUrl != null && !dbUrl.isEmpty()) {
                            props.put("jakarta.persistence.jdbc.url", dbUrl);
                        }
                        if (dbUser != null && !dbUser.isEmpty()) {
                            props.put("jakarta.persistence.jdbc.user", dbUser);
                        }
                        if (dbPassword != null && !dbPassword.isEmpty()) {
                            props.put("jakarta.persistence.jdbc.password", dbPassword);
                        }
                        
                        // Crear EntityManagerFactory con propiedades adicionales
                        if (!props.isEmpty()) {
                            entityManagerFactory = Persistence.createEntityManagerFactory(
                                PERSISTENCE_UNIT, props);
                        } else {
                            entityManagerFactory = Persistence.createEntityManagerFactory(
                                PERSISTENCE_UNIT);
                        }
                        
                        initialized.set(true);
                        LOG.info("EntityManagerFactory inicializado correctamente");
                        
                    } catch (Exception e) {
                        LOG.error("Error al inicializar EntityManagerFactory: {}", 
                                  e.getMessage(), e);
                        throw new RuntimeException(
                            "No se pudo inicializar EntityManagerFactory", e);
                    }
                }
            }
        }
        return entityManagerFactory;
    }

    /**
     * Crea un nuevo EntityManager para operaciones de base de datos.
     * El EntityManager debe cerrarse después de cada uso.
     * 
     * @return EntityManager nuevo
     * @throws IllegalStateException si el factory no está inicializado
     */
    public static EntityManager getEntityManager() {
        EntityManagerFactory emf = getEntityManagerFactory();
        if (emf == null) {
            throw new IllegalStateException(
                "EntityManagerFactory no disponible. Verificar configuración.");
        }
        
        EntityManager em = emf.createEntityManager();
        LOG.debug("EntityManager creado: {}", em);
        return em;
    }

    /**
     * Verifica si el EntityManagerFactory está inicializado.
     * 
     * @return true si está inicializado
     */
    public static boolean isInitialized() {
        return initialized.get() && entityManagerFactory != null;
    }

    /**
     * Cierra el EntityManagerFactory y libera recursos.
     * Debe llamarse al shutting down de la aplicación.
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            LOG.info("Cerrando EntityManagerFactory...");
            entityManagerFactory.close();
            entityManagerFactory = null;
            initialized.set(false);
            LOG.info("EntityManagerFactory cerrado correctamente");
        }
    }

    /**
     * Obtiene las propiedades de configuración de persistencia.
     * Útil para debugging.
     * 
     * @return Mapa con propiedades
     */
    public static Map<String, Object> getPersistenceProperties() {
        EntityManagerFactory emf = getEntityManagerFactory();
        if (emf != null) {
            return emf.getProperties();
        }
        return new HashMap<>();
    }

}
