package com.finanzasapp.util;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.model.entity.Moneda;
import com.finanzasapp.backend.model.entity.TipoCuenta;
import com.finanzasapp.backend.model.entity.CategoriaGasto;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestConexion {

    private static final Logger LOG = LoggerFactory.getLogger(TestConexion.class);

    public static void main(String[] args) {
        LOG.info("========================================");
        LOG.info("VERIFICACION DE BASE DE DATOS");
        LOG.info("========================================");

        EntityManager em = null;
        
        try {
            LOG.info("1. Verificando EntityManagerFactory...");
            JPAConfig.getEntityManagerFactory();
            LOG.info("   OK: EntityManagerFactory funcionando");

            em = JPAUtil.getEntityManager();
            LOG.info("   OK: EntityManager abierto");

            LOG.info("2. Verificando tablas...");
            
            List<Moneda> monedas = em.createQuery("SELECT m FROM Moneda m", Moneda.class).getResultList();
            LOG.info("   Monedas en BD: {}", monedas.size());
            
            List<TipoCuenta> tipos = em.createQuery("SELECT t FROM TipoCuenta t", TipoCuenta.class).getResultList();
            LOG.info("   Tipos de Cuenta en BD: {}", tipos.size());
            
            List<CategoriaGasto> categorias = em.createQuery("SELECT c FROM CategoriaGasto c", CategoriaGasto.class).getResultList();
            LOG.info("   Categorias de Gasto en BD: {}", categorias.size());

            LOG.info("========================================");
            LOG.info("BASE DE DATOS LISTA PARA USAR");
            LOG.info("========================================");

        } catch (Exception e) {
            LOG.error("ERROR: {}", e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
            JPAConfig.closeEntityManagerFactory();
        }
    }
}
