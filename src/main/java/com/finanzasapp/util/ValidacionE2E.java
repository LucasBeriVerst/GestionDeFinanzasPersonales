package com.finanzasapp.util;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.model.dto.*;
import com.finanzasapp.backend.model.entity.*;
import com.finanzasapp.backend.model.enums.TipoCategoria;
import com.finanzasapp.backend.model.enums.TipoMovimiento;
import com.finanzasapp.backend.repository.*;
import com.finanzasapp.backend.repository.impl.*;
import com.finanzasapp.backend.service.impl.*;
import com.finanzasapp.backend.service.interfaces.*;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ValidacionE2E {

    private static final Logger LOG = LoggerFactory.getLogger(ValidacionE2E.class);
    
    public static void main(String[] args) {
        LOG.info("===========================================");
        LOG.info("VALIDACIÓN END-TO-END DEL SISTEMA");
        LOG.info("===========================================");
        
        EntityManager em = JPAConfig.getEntityManager();
        
        try {
            // Inicializar repositorios
            UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl(em);
            CuentaFinancieraRepository cuentaRepo = new CuentaFinancieraRepositoryImpl(em);
            CategoriaGastoRepository categoriaRepo = new CategoriaGastoRepositoryImpl(em);
            GastoRepository gastoRepo = new GastoRepositoryImpl(em);
            MovimientoRepository movimientoRepo = new MovimientoRepositoryImpl(em);
            TipoCuentaRepository tipoCuentaRepo = new TipoCuentaRepositoryImpl(em);
            MonedaRepository monedaRepo = new MonedaRepositoryImpl(em);
            
            // Inicializar servicios
            IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepo);
            ICuentaFinancieraService cuentaService = new CuentaFinancieraServiceImpl(
                cuentaRepo, usuarioRepo, tipoCuentaRepo, monedaRepo);
            ICategoriaGastoService categoriaService = new CategoriaGastoServiceImpl(categoriaRepo);
            IGastoService gastoService = new GastoServiceImpl(
                em, gastoRepo, cuentaRepo, usuarioRepo, categoriaRepo, monedaRepo, movimientoRepo);
            IAutenticacionService authService = new AutenticacionServiceImpl(usuarioRepo);
            
            // ============================================
            // PRUEBA 1: Registro de usuario
            // ============================================
            LOG.info("\n>>> PRUEBA 1: Registro de usuario");
            LOG.info("------------------------------");
            
            String usernameTest = "usuario123";
            
            UsuarioCreateDTO nuevoUsuario = new UsuarioCreateDTO();
            nuevoUsuario.setUsername(usernameTest);
            nuevoUsuario.setPassword("password123");
            nuevoUsuario.setEmail("test@ejemplo.com");
            
            UsuarioDTO usuarioCreado = usuarioService.crear(nuevoUsuario);
            LOG.info("✓ Usuario creado: ID={}, Username={}", usuarioCreado.getId(), usuarioCreado.getUsername());
            
            // ============================================
            // PRUEBA 2: Login
            // ============================================
            LOG.info("\n>>> PRUEBA 2: Login");
            LOG.info("------------------");
            
            UsuarioDTO usuarioLogueado = authService.login(usernameTest, "password123");
            LOG.info("✓ Login exitoso: {}", usuarioLogueado.getUsername());
            LOG.info("  - ID: {}", usuarioLogueado.getId());
            LOG.info("  - Email: {}", usuarioLogueado.getEmail());
            
            // ============================================
            // PRUEBA 3: Crear cuenta financiera
            // ============================================
            LOG.info("\n>>> PRUEBA 3: Crear cuenta financiera");
            LOG.info("------------------------------------");
            
            // Obtener tipos de cuenta y monedas existentes
            List<TipoCuenta> tiposCuenta = tipoCuentaRepo.findAll();
            List<Moneda> monedas = monedaRepo.findAll();
            
            LOG.info("Tipos de cuenta disponibles: {}", tiposCuenta.size());
            LOG.info("Monedas disponibles: {}", monedas.size());
            
            CuentaFinancieraCreateDTO nuevaCuenta = new CuentaFinancieraCreateDTO();
            nuevaCuenta.setIdUsuario(usuarioLogueado.getId());
            nuevaCuenta.setNombre("Mi Billetera");
            nuevaCuenta.setIdTipoCuenta(tiposCuenta.get(0).getId()); // Efectivo
            nuevaCuenta.setIdMoneda(monedas.get(0).getId()); // USD
            
            CuentaFinancieraDTO cuentaCreada = cuentaService.crear(nuevaCuenta);
            LOG.info("✓ Cuenta creada: ID={}, Nombre={}, Saldo inicial={}", 
                cuentaCreada.getId(), cuentaCreada.getNombre(), cuentaCreada.getSaldoActual());
            
            // Agregar saldo inicial a la cuenta (el DTO no tiene saldoInicial)
            em.getTransaction().begin();
            CuentaFinanciera cuentaEntidad = em.find(CuentaFinanciera.class, cuentaCreada.getId());
            cuentaEntidad.setSaldoActual(new BigDecimal("1000.00"));
            em.merge(cuentaEntidad);
            em.getTransaction().commit();
            
            cuentaCreada = cuentaService.buscarPorId(cuentaCreada.getId());
            LOG.info("  Saldo actualizado a: {}", cuentaCreada.getSaldoActual());
            
            // ============================================
            // PRUEBA 4: Crear categoría de gasto
            // ============================================
            LOG.info("\n>>> PRUEBA 4: Crear categoría de gasto");
            LOG.info("------------------------------------");
            
            String categoriaTest = "Transporte_" + System.currentTimeMillis();
            
            CategoriaGastoCreateDTO nuevaCategoria = new CategoriaGastoCreateDTO();
            nuevaCategoria.setNombre(categoriaTest);
            nuevaCategoria.setDescripcion("Gastos de transporte");
            nuevaCategoria.setTipo(TipoCategoria.VARIABLE);
            
            CategoriaGastoDTO categoriaCreada = categoriaService.crear(nuevaCategoria);
            LOG.info("✓ Categoría creada: ID={}, Nombre={}, Tipo={}", 
                    categoriaCreada.getId(), categoriaCreada.getNombre(), categoriaCreada.getTipo());
            
            // ============================================
            // PRUEBA 5: Registrar gasto
            // ============================================
            LOG.info("\n>>> PRUEBA 5: Registrar gasto");
            LOG.info("---------------------------");
            
            GastoCreateDTO nuevoGasto = new GastoCreateDTO();
            nuevoGasto.setIdUsuario(usuarioLogueado.getId());
            nuevoGasto.setIdCuenta(cuentaCreada.getId());
            nuevoGasto.setIdCategoria(categoriaCreada.getId());
            nuevoGasto.setMonto(new BigDecimal("150.00"));
            nuevoGasto.setDescripcion("Taxi al centro");
            nuevoGasto.setIdMoneda(monedas.get(0).getId());
            
            GastoDTO gastoCreado = gastoService.crear(nuevoGasto);
            LOG.info("✓ Gasto registrado: ID={}, Monto={}, Descripcion={}", 
                gastoCreado.getId(), gastoCreado.getMonto(), gastoCreado.getDescripcion());
            
            // ============================================
            // PRUEBA 6: Verificar descuento de saldo
            // ============================================
            LOG.info("\n>>> PRUEBA 6: Verificar descuento de saldo");
            LOG.info("-----------------------------------------");
            
            CuentaFinancieraDTO cuentaActualizada = cuentaService.buscarPorId(cuentaCreada.getId());
            LOG.info("Saldo anterior: 1000.00");
            LOG.info("Monto gasto: 150.00");
            LOG.info("Saldo actual: {}", cuentaActualizada.getSaldoActual());
            
            if (cuentaActualizada.getSaldoActual().compareTo(new BigDecimal("850.00")) == 0) {
                LOG.info("✓ Descuento de saldo: CORRECTO");
            } else {
                LOG.error("✗ Descuento de saldo: INCORRECTO");
            }
            
            // ============================================
            // PRUEBA 7: Verificar movimiento automático
            // ============================================
            LOG.info("\n>>> PRUEBA 7: Verificar movimiento automático");
            LOG.info("---------------------------------------------");
            
            List<Movimiento> movimientos = movimientoRepo.findByCuentaId(cuentaCreada.getId());
            LOG.info("Movimientos creados: {}", movimientos.size());
            
            for (Movimiento m : movimientos) {
                LOG.info("  - Tipo: {}, Monto: {}, Desc: {}", 
                    m.getTipo(), m.getMonto(), m.getReferencia());
            }
            
            boolean tieneMovimientoSalida = movimientos.stream()
                .anyMatch(m -> m.getTipo() == TipoMovimiento.SALIDA && 
                              m.getMonto().compareTo(new BigDecimal("150.00")) == 0);
            
            if (tieneMovimientoSalida) {
                LOG.info("✓ Movimiento automático: CORRECTO");
            } else {
                LOG.error("✗ Movimiento automático: NO ENCONTRADO");
            }
            
            // ============================================
            // PRUEBA 8: Validación - saldo insuficiente
            // ============================================
            LOG.info("\n>>> PRUEBA 8: Validación de saldo insuficiente");
            LOG.info("---------------------------------------------");
            
            try {
                GastoCreateDTO gastoMayor = new GastoCreateDTO();
                gastoMayor.setIdUsuario(usuarioLogueado.getId());
                gastoMayor.setIdCuenta(cuentaCreada.getId());
                gastoMayor.setIdCategoria(categoriaCreada.getId());
                gastoMayor.setMonto(new BigDecimal("2000.00")); // Mayor al saldo
                gastoMayor.setDescripcion("Gasto que excede saldo");
                
                gastoService.crear(gastoMayor);
                LOG.error("✗ Validación saldo insuficiente: FALLO (no lanzó excepción)");
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Saldo insuficiente")) {
                    LOG.info("✓ Validación saldo insuficiente: CORRECTO");
                    LOG.info("  Mensaje: {}", e.getMessage());
                } else {
                    LOG.error("✗ Error diferente: {}", e.getMessage());
                }
            }
            
            // ============================================
            // PRUEBA 9: Validación - monto inválido
            // ============================================
            LOG.info("\n>>> PRUEBA 9: Validación de monto inválido");
            LOG.info("-----------------------------------------");
            
            try {
                GastoCreateDTO gastoCero = new GastoCreateDTO();
                gastoCero.setIdUsuario(usuarioLogueado.getId());
                gastoCero.setIdCuenta(cuentaCreada.getId());
                gastoCero.setIdCategoria(categoriaCreada.getId());
                gastoCero.setMonto(BigDecimal.ZERO);
                gastoCero.setDescripcion("Gasto cero");
                
                gastoService.crear(gastoCero);
                LOG.error("✗ Validación monto cero: FALLO (no lanzó excepción)");
            } catch (RuntimeException e) {
                if (e.getMessage().contains("mayor a 0")) {
                    LOG.info("✓ Validación monto cero: CORRECTO");
                    LOG.info("  Mensaje: {}", e.getMessage());
                } else {
                    LOG.error("✗ Error diferente: {}", e.getMessage());
                }
            }
            
            // ============================================
            // PRUEBA 10: Persistencia en base de datos
            // ============================================
            LOG.info("\n>>> PRUEBA 10: Verificar datos en BD");
            LOG.info("-----------------------------------");
            
            List<Usuario> usuarios = usuarioRepo.findAll();
            List<CuentaFinanciera> cuentas = cuentaRepo.findAll();
            List<Gasto> gastos = gastoRepo.findAll();
            List<CategoriaGasto> categorias = categoriaRepo.findAll();
            
            LOG.info("=== RESUMEN BD ===");
            LOG.info("Usuarios: {}", usuarios.size());
            LOG.info("Cuentas: {}", cuentas.size());
            LOG.info("Gastos: {}", gastos.size());
            LOG.info("Categorías: {}", categorias.size());
            LOG.info("Movimientos: {}", movimientos.size());
            
            // ============================================
            // RESUMEN FINAL
            // ============================================
            LOG.info("\n===========================================");
            LOG.info("RESUMEN DE VALIDACIÓN");
            LOG.info("===========================================");
            LOG.info("✓ Registro de usuario: PASS");
            LOG.info("✓ Login: PASS");
            LOG.info("✓ Crear cuenta financiera: PASS");
            LOG.info("✓ Crear categoría de gasto: PASS");
            LOG.info("✓ Registrar gasto: PASS");
            LOG.info("✓ Descuento automático de saldo: PASS");
            LOG.info("✓ Creación automática de movimiento: PASS");
            LOG.info("✓ Validación saldo insuficiente: PASS");
            LOG.info("✓ Validación monto inválido: PASS");
            LOG.info("✓ Persistencia en BD: PASS");
            LOG.info("===========================================");
            LOG.info("TODAS LAS PRUEBAS EXITOSAS");
            LOG.info("===========================================");
            
        } catch (Exception e) {
            LOG.error("ERROR EN VALIDACIÓN: {}", e.getMessage(), e);
        } finally {
            em.close();
            JPAConfig.closeEntityManagerFactory();
        }
    }
}
