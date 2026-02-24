package com.finanzasapp.backend.validator;

import com.finanzasapp.backend.model.dto.CuentaFinancieraCreateDTO;
import java.util.ArrayList;
import java.util.List;

public class CuentaFinancieraValidator {

    private CuentaFinancieraValidator() {
    }

    public static List<String> validarCreacion(CuentaFinancieraCreateDTO dto) {
        List<String> errores = new ArrayList<>();

        errores.addAll(ValidadorUtil.validarNombre(dto.getNombre(), "El nombre de la cuenta"));
        errores.addAll(ValidadorUtil.validarId(dto.getIdUsuario(), "usuario"));
        errores.addAll(ValidadorUtil.validarId(dto.getIdTipoCuenta(), "tipo de cuenta"));
        errores.addAll(ValidadorUtil.validarId(dto.getIdMoneda(), "moneda"));
        errores.addAll(ValidadorUtil.validarMontoNoNegativo(dto.getSaldoInicial(), "El saldo inicial"));

        return errores;
    }
}
