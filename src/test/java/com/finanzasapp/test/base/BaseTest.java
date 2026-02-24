package com.finanzasapp.test.base;

import com.finanzasapp.database.JPAConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

public abstract class BaseTest {

    protected EntityManager createEntityManager() {
        return JPAConfig.getEntityManager();
    }

    protected void cleanup(EntityManager em) {
        if (em != null && em.isOpen()) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
