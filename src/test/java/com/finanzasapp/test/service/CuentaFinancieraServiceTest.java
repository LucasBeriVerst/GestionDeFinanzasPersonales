package com.finanzasapp.test.service;

import com.finanzasapp.backend.exception.ValidationException;
import com.finanzasapp.backend.model.dto.*;
import com.finanzasapp.backend.model.entity.*;
import com.finanzasapp.backend.repository.*;
import com.finanzasapp.backend.repository.impl.*;
import com.finanzasapp.backend.service.impl.*;
import com.finanzasapp.test.base.BaseTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CuentaFinancieraServiceTest extends BaseTest {

    private EntityManager em;
    private CuentaFinancieraServiceImpl cuentaService;
    private UsuarioServiceImpl usuarioService;
    
    private static Long usuarioId;
    private static Long tipoCuentaId;
    private static Long monedaId;

    @BeforeEach
    public void setup() {
        em = createEntityManager();
        
        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(em);
        usuarioService = new UsuarioServiceImpl(usuarioRepository);
        
        TipoCuentaRepository tipoCuentaRepository = new TipoCuentaRepositoryImpl(em);
        MonedaRepository monedaRepository = new MonedaRepositoryImpl(em);
        CuentaFinancieraRepository cuentaRepository = new CuentaFinancieraRepositoryImpl(em);
        
        cuentaService = new CuentaFinancieraServiceImpl(
            cuentaRepository, usuarioRepository, tipoCuentaRepository, monedaRepository);

        if (tipoCuentaId == null) {
            List<TipoCuenta> tipos = tipoCuentaRepository.findAll();
            if (!tipos.isEmpty()) tipoCuentaId = tipos.get(0).getId();
        }
        
        if (monedaId == null) {
            List<Moneda> monedas = monedaRepository.findAll();
            if (!monedas.isEmpty()) monedaId = monedas.get(0).getId();
        }
        
        if (usuarioId == null) {
            String username = "usercta" + System.currentTimeMillis();
            UsuarioCreateDTO dto = new UsuarioCreateDTO();
            dto.setUsername(username);
            dto.setPassword("password123");
            dto.setEmail("cta@test.com");
            usuarioId = usuarioService.crear(dto).getId();
        }
    }

    @AfterEach
    public void teardown() {
        cleanup(em);
    }

    @Test
    @Order(1)
    @DisplayName("TC-CUENTA-01: Crear cuenta exitosamente")
    public void testCrearCuentaExitosa() {
        CuentaFinancieraCreateDTO dto = new CuentaFinancieraCreateDTO();
        dto.setNombre("Cuenta Test " + System.currentTimeMillis());
        dto.setIdUsuario(usuarioId);
        dto.setIdTipoCuenta(tipoCuentaId);
        dto.setIdMoneda(monedaId);
        dto.setSaldoInicial(new BigDecimal("1000.00"));

        CuentaFinancieraDTO resultado = cuentaService.crear(dto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(new BigDecimal("1000.00"), resultado.getSaldoActual());
        System.out.println("✓ TC-CUENTA-01: Cuenta creada con saldo: " + resultado.getSaldoActual());
    }

    @Test
    @Order(2)
    @DisplayName("TC-CUENTA-02: Validar nombre requerido")
    public void testNombreRequerido() {
        CuentaFinancieraCreateDTO dto = new CuentaFinancieraCreateDTO();
        dto.setIdUsuario(usuarioId);
        dto.setIdTipoCuenta(tipoCuentaId);
        dto.setIdMoneda(monedaId);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            cuentaService.crear(dto);
        });

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.contains("nombre")));
        System.out.println("✓ TC-CUENTA-02: Validación nombre requerido funciona");
    }

    @Test
    @Order(3)
    @DisplayName("TC-CUENTA-03: Validar usuario requerido")
    public void testUsuarioRequerido() {
        CuentaFinancieraCreateDTO dto = new CuentaFinancieraCreateDTO();
        dto.setNombre("Cuenta Test 2");
        dto.setIdTipoCuenta(tipoCuentaId);
        dto.setIdMoneda(monedaId);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            cuentaService.crear(dto);
        });

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.contains("usuario")));
        System.out.println("✓ TC-CUENTA-03: Validación usuario requerido funciona");
    }

    @Test
    @Order(4)
    @DisplayName("TC-CUENTA-04: Validar saldo negativo")
    public void testSaldoNegativo() {
        CuentaFinancieraCreateDTO dto = new CuentaFinancieraCreateDTO();
        dto.setNombre("Cuenta Test 3");
        dto.setIdUsuario(usuarioId);
        dto.setIdTipoCuenta(tipoCuentaId);
        dto.setIdMoneda(monedaId);
        dto.setSaldoInicial(new BigDecimal("-100.00"));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            cuentaService.crear(dto);
        });

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.contains("negativo")));
        System.out.println("✓ TC-CUENTA-04: Validación saldo negativo funciona");
    }

    @Test
    @Order(5)
    @DisplayName("TC-CUENTA-05: Listar cuentas por usuario")
    public void testListarPorUsuario() {
        List<CuentaFinancieraDTO> cuentas = cuentaService.listarPorUsuario(usuarioId);
        assertNotNull(cuentas);
        System.out.println("✓ TC-CUENTA-05: Cuentas del usuario: " + cuentas.size());
    }
}
