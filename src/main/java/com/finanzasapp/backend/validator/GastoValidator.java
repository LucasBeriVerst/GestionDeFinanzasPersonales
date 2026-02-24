package com.finanzasapp.backend.validator;

import com.finanzasapp.backend.model.dto.GastoCreateDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GastoValidator {

    private GastoValidator() {
    }

    public static List<String> validarCreacion(GastoCreateDTO dto) {
        List<String> errores = new ArrayList<>();

        errores.addAll(ValidadorUtil.validarId(dto.getIdUsuario(), "usuario"));
        errores.addAll(ValidadorUtil.validarId(dto.getIdCuenta(), "cuenta"));
        errores.addAll(ValidadorUtil.validarId(dto.getIdCategoria(), "categoría"));
        errores.addAll(ValidadorUtil.validarMonto(dto.getMonto(), "El monto"));

        return errores;
    }

    public static List<String> validarSaldo(BigDecimal saldoActual, BigDecimal montoGasto) {
        List<String> errores = new ArrayList<>();
        
        if (saldoActual == null || montoGasto == null) {
            return errores;
        }
        
        if (saldoActual.compareTo(montoGasto) < 0) {
            errores.add("Saldo insuficiente en la cuenta. Saldo actual: " + saldoActual);
        }
        
        return errores;
    }
}
