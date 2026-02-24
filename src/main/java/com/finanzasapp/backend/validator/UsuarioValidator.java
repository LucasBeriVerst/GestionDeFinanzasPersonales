package com.finanzasapp.backend.validator;

import com.finanzasapp.backend.model.dto.UsuarioCreateDTO;
import java.util.ArrayList;
import java.util.List;

public class UsuarioValidator {

    private UsuarioValidator() {
    }

    public static List<String> validarCreacion(UsuarioCreateDTO dto) {
        List<String> errores = new ArrayList<>();

        errores.addAll(ValidadorUtil.validarUsername(dto.getUsername()));
        errores.addAll(ValidadorUtil.validarPassword(dto.getPassword()));
        
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            errores.addAll(ValidadorUtil.validarEmail(dto.getEmail()));
        }

        return errores;
    }
}
