package com.finanzasapp.test.service;

import com.finanzasapp.backend.exception.ValidationException;
import com.finanzasapp.backend.model.dto.UsuarioCreateDTO;
import com.finanzasapp.backend.model.dto.UsuarioDTO;
import com.finanzasapp.backend.model.entity.Usuario;
import com.finanzasapp.backend.repository.UsuarioRepository;
import com.finanzasapp.backend.repository.impl.UsuarioRepositoryImpl;
import com.finanzasapp.backend.service.impl.UsuarioServiceImpl;
import com.finanzasapp.test.base.BaseTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioServiceTest extends BaseTest {

    private EntityManager em;
    private UsuarioServiceImpl usuarioService;
    private UsuarioRepository usuarioRepository;
    private static String usernameBase = "user" + System.currentTimeMillis();

    @BeforeEach
    public void setup() {
        em = createEntityManager();
        usuarioRepository = new UsuarioRepositoryImpl(em);
        usuarioService = new UsuarioServiceImpl(usuarioRepository);
    }

    @AfterEach
    public void teardown() {
        cleanup(em);
    }

    @Test
    @Order(1)
    @DisplayName("TC-USUARIO-01: Crear usuario exitosamente")
    public void testCrearUsuarioExitoso() {
        String username = usernameBase + "_1";
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUsername(username);
        dto.setPassword("password123");
        dto.setEmail("test@test.com");

        UsuarioDTO resultado = usuarioService.crear(dto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(username, resultado.getUsername());
        System.out.println("✓ TC-USUARIO-01: Usuario creado - " + username);
    }

    @Test
    @Order(2)
    @DisplayName("TC-USUARIO-02: Crear usuario con username duplicado")
    public void testCrearUsuarioDuplicado() {
        String username = usernameBase + "_1";
        
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUsername(username);
        dto.setPassword("password123");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usuarioService.crear(dto);
        });

        assertTrue(exception.getMessage().contains("ya existe"));
        System.out.println("✓ TC-USUARIO-02: Validación usuario duplicado funciona");
    }

    @Test
    @Order(3)
    @DisplayName("TC-USUARIO-03: Validar username requerido")
    public void testUsuarioRequerido() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setPassword("password123");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usuarioService.crear(dto);
        });

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.contains("obligatorio")));
        System.out.println("✓ TC-USUARIO-03: Validación usuario requerido funciona");
    }

    @Test
    @Order(4)
    @DisplayName("TC-USUARIO-04: Validar password requerido")
    public void testPasswordRequerido() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUsername("newuser");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usuarioService.crear(dto);
        });

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.contains("Contraseña")));
        System.out.println("✓ TC-USUARIO-04: Validación password requerido funciona");
    }

    @Test
    @Order(5)
    @DisplayName("TC-USUARIO-05: Validar longitud mínima password")
    public void testPasswordLongitudMinima() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO();
        dto.setUsername("newuser123");
        dto.setPassword("abc");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            usuarioService.crear(dto);
        });

        assertTrue(exception.getErrores().stream().anyMatch(e -> e.contains("6 caracteres")));
        System.out.println("✓ TC-USUARIO-05: Validación longitud mínima password funciona");
    }

    @Test
    @Order(6)
    @DisplayName("TC-USUARIO-06: Buscar usuario por ID")
    public void testBuscarPorId() {
        List<UsuarioDTO> usuarios = usuarioService.listarActivos();
        assertFalse(usuarios.isEmpty());

        UsuarioDTO usuario = usuarios.get(0);
        UsuarioDTO resultado = usuarioService.buscarPorId(usuario.getId());
        
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        System.out.println("✓ TC-USUARIO-06: Búsqueda por ID funciona");
    }

    @Test
    @Order(7)
    @DisplayName("TC-USUARIO-07: Listar usuarios activos")
    public void testListarActivos() {
        List<UsuarioDTO> usuarios = usuarioService.listarActivos();
        assertNotNull(usuarios);
        assertTrue(usuarios.size() > 0);
        System.out.println("✓ TC-USUARIO-07: Total usuarios activos: " + usuarios.size());
    }

    @Test
    @Order(8)
    @DisplayName("TC-USUARIO-08: Eliminar usuario (soft delete)")
    public void testEliminarUsuario() {
        List<UsuarioDTO> usuarios = usuarioService.listarActivos();
        assertFalse(usuarios.isEmpty());
        
        Long id = usuarios.get(0).getId();
        usuarioService.eliminar(id);

        UsuarioDTO eliminado = usuarioService.buscarPorId(id);
        assertFalse(eliminado.getActivo());
        System.out.println("✓ TC-USUARIO-08: Soft delete funciona");
    }
}
