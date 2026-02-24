package com.finanzasapp.util;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.model.entity.*;
import com.finanzasapp.backend.model.enums.*;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ValidacionSistema {

    private static final Logger LOG = LoggerFactory.getLogger(ValidacionSistema.class);

    public static void main(String[] args) {
        LOG.info("========================================");
        LOG.info("VALIDACION FUNCIONAL DEL SISTEMA");
        LOG.info("========================================");

        EntityManager em = null;

        try {
            LOG.info("1. Inicializando sistema...");
            JPAConfig.getEntityManagerFactory();
            em = JPAUtil.getEntityManager();
            LOG.info("   OK: Sistema inicializado");

            LOG.info("2. Verificando datos existentes...");
            List<Moneda> monedas = em.createQuery("SELECT m FROM Moneda m", Moneda.class).getResultList();
            List<TipoCuenta> tipos = em.createQuery("SELECT t FROM TipoCuenta t", TipoCuenta.class).getResultList();
            List<CategoriaGasto> cats = em.createQuery("SELECT c FROM CategoriaGasto c", CategoriaGasto.class).getResultList();
            
            LOG.info("   Monedas: {}", monedas.size());
            LOG.info("   Tipos Cuenta: {}", tipos.size());
            LOG.info("   Categorías: {}", cats.size());

            if (monedas.isEmpty() || tipos.isEmpty() || cats.isEmpty()) {
                LOG.info("3. Insertando datos iniciales...");
                em.getTransaction().begin();

                Moneda usd = new Moneda("USD", "Dólar Estadounidense", "$");
                usd.setPredeterminada(true);
                usd.setTipoCambioBase(BigDecimal.ONE);
                em.persist(usd);

                Moneda eur = new Moneda("EUR", "Euro", "€");
                em.persist(eur);

                TipoCuenta efectivo = new TipoCuenta("Efectivo", "Dinero en efectivo");
                efectivo.setPermiteDepositos(true);
                efectivo.setPermiteRetiros(true);
                em.persist(efectivo);

                TipoCuenta banco = new TipoCuenta("Banco", "Cuenta bancaria");
                banco.setPermiteDepositos(true);
                banco.setPermiteRetiros(true);
                em.persist(banco);

                CategoriaGasto alimentacion = new CategoriaGasto(
                    "Alimentación", "Gastos de comida y supermercado", "food", TipoCategoria.VARIABLE);
                em.persist(alimentacion);

                CategoriaGasto servicios = new CategoriaGasto(
                    "Servicios", "Luz, agua, internet, teléfono", "bill", TipoCategoria.FIJO);
                em.persist(servicios);

                CategoriaGasto transporte = new CategoriaGasto(
                    "Transporte", "Combustible, transporte público", "car", TipoCategoria.VARIABLE);
                em.persist(transporte);

                em.getTransaction().commit();
                LOG.info("   OK: Datos iniciales insertados");
            }

            LOG.info("4. Verificando estructura de tablas en BD...");
            Long usuarios = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
            Long cuentas = em.createQuery("SELECT COUNT(c) FROM CuentaFinanciera c", Long.class).getSingleResult();
            Long gastos = em.createQuery("SELECT COUNT(g) FROM Gasto g", Long.class).getSingleResult();
            Long movimientos = em.createQuery("SELECT COUNT(m) FROM Movimiento m", Long.class).getSingleResult();

            LOG.info("   === ESTADO ACTUAL DE LA BD ===");
            LOG.info("   Usuarios: {}", usuarios);
            LOG.info("   Cuentas: {}", cuentas);
            LOG.info("   Gastos: {}", gastos);
            LOG.info("   Movimientos: {}", movimientos);

            LOG.info("5. Listando datos disponibles...");
            LOG.info("   === MONEDAS ===");
            for (Moneda m : em.createQuery("SELECT m FROM Moneda m", Moneda.class).getResultList()) {
                LOG.info("   - {} ({}) - Predeterminada: {}", m.getCodigoISO(), m.getSimbolo(), m.getPredeterminada());
            }

            LOG.info("   === TIPOS DE CUENTA ===");
            for (TipoCuenta t : em.createQuery("SELECT t FROM TipoCuenta t", TipoCuenta.class).getResultList()) {
                LOG.info("   - {} - Depósitos: {}, Retiros: {}", t.getNombre(), t.getPermiteDepositos(), t.getPermiteRetiros());
            }

            LOG.info("   === CATEGORÍAS DE GASTO ===");
            for (CategoriaGasto c : em.createQuery("SELECT c FROM CategoriaGasto c", CategoriaGasto.class).getResultList()) {
                LOG.info("   - {} ({}) - Tipo: {}", c.getNombre(), c.getIcono(), c.getTipo());
            }

            LOG.info("========================================");
            LOG.info("VALIDACION COMPLETA - SISTEMA LISTO");
            LOG.info("========================================");
            LOG.info("El sistema está preparado para usar la aplicación.");
            LOG.info("Ejecutar Main.java para iniciar la interfaz Swing.");

        } catch (Exception e) {
            LOG.error("ERROR EN VALIDACION: {}", e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) em.close();
            JPAConfig.closeEntityManagerFactory();
        }
    }
}
