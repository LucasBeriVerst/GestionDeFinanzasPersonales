package com.finanzasapp.backend.service.impl;

import com.finanzasapp.backend.exception.ValidationException;
import com.finanzasapp.backend.model.dto.GastoCreateDTO;
import com.finanzasapp.backend.model.dto.GastoDTO;
import com.finanzasapp.backend.model.entity.CategoriaGasto;
import com.finanzasapp.backend.model.entity.CuentaFinanciera;
import com.finanzasapp.backend.model.entity.Gasto;
import com.finanzasapp.backend.model.entity.Moneda;
import com.finanzasapp.backend.model.entity.Movimiento;
import com.finanzasapp.backend.model.entity.Usuario;
import com.finanzasapp.backend.model.enums.TipoMovimiento;
import com.finanzasapp.backend.repository.CategoriaGastoRepository;
import com.finanzasapp.backend.repository.CuentaFinancieraRepository;
import com.finanzasapp.backend.repository.GastoRepository;
import com.finanzasapp.backend.repository.MonedaRepository;
import com.finanzasapp.backend.repository.MovimientoRepository;
import com.finanzasapp.backend.repository.UsuarioRepository;
import com.finanzasapp.backend.service.interfaces.IGastoService;
import com.finanzasapp.backend.validator.GastoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class GastoServiceImpl implements IGastoService {

    private final EntityManager em;
    private final GastoRepository gastoRepository;
    private final CuentaFinancieraRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaGastoRepository categoriaRepository;
    private final MonedaRepository monedaRepository;
    private final MovimientoRepository movimientoRepository;

    public GastoServiceImpl(
            EntityManager em,
            GastoRepository gastoRepository,
            CuentaFinancieraRepository cuentaRepository,
            UsuarioRepository usuarioRepository,
            CategoriaGastoRepository categoriaRepository,
            MonedaRepository monedaRepository,
            MovimientoRepository movimientoRepository) {
        this.em = em;
        this.gastoRepository = gastoRepository;
        this.cuentaRepository = cuentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.monedaRepository = monedaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public GastoDTO crear(GastoCreateDTO dto) {
        List<String> errores = GastoValidator.validarCreacion(dto);
        
        if (dto.getIdUsuario() != null && 
            usuarioRepository.findById(dto.getIdUsuario()).isEmpty()) {
            errores.add("Usuario no encontrado");
        }
        
        if (dto.getIdCuenta() != null && 
            cuentaRepository.findById(dto.getIdCuenta()).isEmpty()) {
            errores.add("Cuenta no encontrada");
        }
        
        if (dto.getIdCategoria() != null && 
            categoriaRepository.findById(dto.getIdCategoria()).isEmpty()) {
            errores.add("Categoría no encontrada");
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();

            Usuario usuario = em.find(Usuario.class, dto.getIdUsuario());
            if (usuario == null) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }

            CuentaFinanciera cuenta = em.find(CuentaFinanciera.class, dto.getIdCuenta());
            if (cuenta == null) {
                throw new IllegalArgumentException("Cuenta no encontrada");
            }

            if (cuenta.getSaldoActual().compareTo(dto.getMonto()) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente en la cuenta");
            }

            CategoriaGasto categoria = em.find(CategoriaGasto.class, dto.getIdCategoria());
            if (categoria == null) {
                throw new IllegalArgumentException("Categoría no encontrada");
            }

            Moneda moneda = null;
            if (dto.getIdMoneda() != null) {
                moneda = em.find(Moneda.class, dto.getIdMoneda());
            }

            cuenta.setSaldoActual(cuenta.getSaldoActual().subtract(dto.getMonto()));
            em.merge(cuenta);

            Gasto gasto = new Gasto(usuario, cuenta, categoria, dto.getMonto(), dto.getDescripcion());
            if (moneda != null) {
                gasto.setMoneda(moneda);
            }
            em.persist(gasto);

            Movimiento movimiento = new Movimiento(
                cuenta,
                TipoMovimiento.SALIDA,
                dto.getMonto(),
                "Gasto: " + (dto.getDescripcion() != null ? dto.getDescripcion() : categoria.getNombre())
            );
            movimiento.setGasto(gasto);
            em.persist(movimiento);

            tx.commit();

            return toDTO(gasto, usuario, cuenta, categoria, moneda);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al registrar gasto: " + e.getMessage(), e);
        }
    }

    @Override
    public GastoDTO actualizar(Long id, GastoCreateDTO dto) {
        Gasto gasto = gastoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));

        if (dto.getMonto() != null) {
            gasto.setMonto(dto.getMonto());
        }
        if (dto.getDescripcion() != null) {
            gasto.setDescripcion(dto.getDescripcion());
        }
        if (dto.getIdCategoria() != null) {
            CategoriaGasto categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            gasto.setCategoria(categoria);
        }

        gasto = gastoRepository.save(gasto);
        return toDTO(gasto, gasto.getUsuario(), gasto.getCuenta(), gasto.getCategoria(), gasto.getMoneda());
    }

    @Override
    public GastoDTO buscarPorId(Long id) {
        Gasto gasto = gastoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));
        return toDTO(gasto, gasto.getUsuario(), gasto.getCuenta(), gasto.getCategoria(), gasto.getMoneda());
    }

    @Override
    public List<GastoDTO> listarPorUsuario(Long usuarioId) {
        return gastoRepository.findByUsuarioIdAndActivoTrue(usuarioId).stream()
            .map(g -> toDTO(g, g.getUsuario(), g.getCuenta(), g.getCategoria(), g.getMoneda()))
            .collect(Collectors.toList());
    }

    @Override
    public List<GastoDTO> listarPorCuenta(Long cuentaId) {
        return gastoRepository.findByCuentaIdAndActivoTrue(cuentaId).stream()
            .map(g -> toDTO(g, g.getUsuario(), g.getCuenta(), g.getCategoria(), g.getMoneda()))
            .collect(Collectors.toList());
    }

    @Override
    public List<GastoDTO> listarTodos() {
        return gastoRepository.findAll().stream()
            .map(g -> toDTO(g, g.getUsuario(), g.getCuenta(), g.getCategoria(), g.getMoneda()))
            .collect(Collectors.toList());
    }

    @Override
    public List<GastoDTO> listarActivos() {
        return gastoRepository.findByActivoTrue().stream()
            .map(g -> toDTO(g, g.getUsuario(), g.getCuenta(), g.getCategoria(), g.getMoneda()))
            .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        Gasto gasto = gastoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));
        gasto.softDelete();
        gastoRepository.save(gasto);
    }

    private GastoDTO toDTO(Gasto gasto, Usuario usuario, CuentaFinanciera cuenta, 
                           CategoriaGasto categoria, Moneda moneda) {
        GastoDTO dto = new GastoDTO();
        dto.setId(gasto.getId());
        dto.setIdUsuario(usuario != null ? usuario.getId() : null);
        dto.setNombreUsuario(usuario != null ? usuario.getUsername() : null);
        dto.setIdCuenta(cuenta != null ? cuenta.getId() : null);
        dto.setNombreCuenta(cuenta != null ? cuenta.getNombre() : null);
        dto.setIdCategoria(categoria != null ? categoria.getId() : null);
        dto.setNombreCategoria(categoria != null ? categoria.getNombre() : null);
        dto.setMonto(gasto.getMonto());
        dto.setDescripcion(gasto.getDescripcion());
        dto.setFecha(gasto.getFecha());
        dto.setIdMoneda(moneda != null ? moneda.getId() : null);
        dto.setNombreMoneda(moneda != null ? moneda.getNombre() : null);
        dto.setActivo(gasto.getActivo());
        return dto;
    }
}
