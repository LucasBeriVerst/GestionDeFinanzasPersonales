package com.finanzasapp.service.impl;

import com.finanzasapp.model.dto.CuentaFinancieraCreateDTO;
import com.finanzasapp.model.dto.CuentaFinancieraDTO;
import com.finanzasapp.model.entity.CuentaFinanciera;
import com.finanzasapp.model.entity.Moneda;
import com.finanzasapp.model.entity.TipoCuenta;
import com.finanzasapp.model.entity.Usuario;
import com.finanzasapp.repository.CuentaFinancieraRepository;
import com.finanzasapp.repository.MonedaRepository;
import com.finanzasapp.repository.TipoCuentaRepository;
import com.finanzasapp.repository.UsuarioRepository;
import com.finanzasapp.service.interfaces.ICuentaFinancieraService;
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
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (dto.getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(dto.getIdTipoCuenta())
            .orElseThrow(() -> new IllegalArgumentException("Tipo de cuenta no encontrado"));

        Moneda moneda = monedaRepository.findById(dto.getIdMoneda())
            .orElseThrow(() -> new IllegalArgumentException("Moneda no encontrada"));

        CuentaFinanciera cuenta = new CuentaFinanciera(usuario, dto.getNombre(), tipoCuenta, moneda);
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
