package com.finanzasapp.backend.service.impl;

import com.finanzasapp.backend.exception.ValidationException;
import com.finanzasapp.backend.model.dto.CuentaFinancieraCreateDTO;
import com.finanzasapp.backend.model.dto.CuentaFinancieraDTO;
import com.finanzasapp.backend.model.entity.CuentaFinanciera;
import com.finanzasapp.backend.model.entity.Moneda;
import com.finanzasapp.backend.model.entity.TipoCuenta;
import com.finanzasapp.backend.model.entity.Usuario;
import com.finanzasapp.backend.repository.CuentaFinancieraRepository;
import com.finanzasapp.backend.repository.MonedaRepository;
import com.finanzasapp.backend.repository.TipoCuentaRepository;
import com.finanzasapp.backend.repository.UsuarioRepository;
import com.finanzasapp.backend.service.interfaces.ICuentaFinancieraService;
import com.finanzasapp.backend.validator.CuentaFinancieraValidator;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CuentaFinancieraServiceImpl implements ICuentaFinancieraService {

    private final CuentaFinancieraRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoCuentaRepository tipoCuentaRepository;
    private final MonedaRepository monedaRepository;

    public CuentaFinancieraServiceImpl(
            CuentaFinancieraRepository cuentaRepository,
            UsuarioRepository usuarioRepository,
            TipoCuentaRepository tipoCuentaRepository,
            MonedaRepository monedaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoCuentaRepository = tipoCuentaRepository;
        this.monedaRepository = monedaRepository;
    }

    @Override
    public CuentaFinancieraDTO crear(CuentaFinancieraCreateDTO dto) {
        List<String> errores = CuentaFinancieraValidator.validarCreacion(dto);
        
        if (dto.getIdUsuario() != null && 
            usuarioRepository.findById(dto.getIdUsuario()).isEmpty()) {
            errores.add("Usuario no encontrado");
        }
        
        if (dto.getIdTipoCuenta() != null && 
            tipoCuentaRepository.findById(dto.getIdTipoCuenta()).isEmpty()) {
            errores.add("Tipo de cuenta no encontrado");
        }
        
        if (dto.getIdMoneda() != null && 
            monedaRepository.findById(dto.getIdMoneda()).isEmpty()) {
            errores.add("Moneda no encontrada");
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).get();
        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(dto.getIdTipoCuenta()).get();
        Moneda moneda = monedaRepository.findById(dto.getIdMoneda()).get();

        CuentaFinanciera cuenta = new CuentaFinanciera(usuario, dto.getNombre(), tipoCuenta, moneda);
        cuenta.setSaldoActual(dto.getSaldoInicial() != null ? dto.getSaldoInicial() : BigDecimal.ZERO);
        cuenta = cuentaRepository.save(cuenta);

        return toDTO(cuenta);
    }

    @Override
    public CuentaFinancieraDTO actualizar(Long id, CuentaFinancieraCreateDTO dto) {
        CuentaFinanciera cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        if (dto.getNombre() != null && !dto.getNombre().isEmpty()) {
            cuenta.setNombre(dto.getNombre());
        }
        if (dto.getIdTipoCuenta() != null) {
            TipoCuenta tipoCuenta = tipoCuentaRepository.findById(dto.getIdTipoCuenta())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de cuenta no encontrado"));
            cuenta.setTipoCuenta(tipoCuenta);
        }
        if (dto.getIdMoneda() != null) {
            Moneda moneda = monedaRepository.findById(dto.getIdMoneda())
                .orElseThrow(() -> new IllegalArgumentException("Moneda no encontrada"));
            cuenta.setMoneda(moneda);
        }

        cuenta = cuentaRepository.save(cuenta);
        return toDTO(cuenta);
    }

    @Override
    public CuentaFinancieraDTO buscarPorId(Long id) {
        CuentaFinanciera cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        return toDTO(cuenta);
    }

    @Override
    public List<CuentaFinancieraDTO> listarPorUsuario(Long usuarioId) {
        return cuentaRepository.findByUsuarioIdAndActivoTrue(usuarioId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<CuentaFinancieraDTO> listarTodos() {
        return cuentaRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<CuentaFinancieraDTO> listarActivos() {
        return cuentaRepository.findByActivoTrue().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        CuentaFinanciera cuenta = cuentaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        cuenta.softDelete();
        cuentaRepository.save(cuenta);
    }

    private CuentaFinancieraDTO toDTO(CuentaFinanciera cuenta) {
        CuentaFinancieraDTO dto = new CuentaFinancieraDTO();
        dto.setId(cuenta.getId());
        dto.setNombre(cuenta.getNombre());
        dto.setIdUsuario(cuenta.getUsuario().getId());
        dto.setNombreUsuario(cuenta.getUsuario().getUsername());
        dto.setIdTipoCuenta(cuenta.getTipoCuenta().getId());
        dto.setNombreTipoCuenta(cuenta.getTipoCuenta().getNombre());
        dto.setIdMoneda(cuenta.getMoneda().getId());
        dto.setNombreMoneda(cuenta.getMoneda().getNombre());
        dto.setSimboloMoneda(cuenta.getMoneda().getSimbolo());
        dto.setSaldoActual(cuenta.getSaldoActual());
        dto.setFechaApertura(cuenta.getFechaCreacion());
        dto.setActivo(cuenta.getActivo());
        return dto;
    }
}
