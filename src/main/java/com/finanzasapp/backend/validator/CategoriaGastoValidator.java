package com.finanzasapp.backend.validator;

import com.finanzasapp.backend.model.dto.CategoriaGastoCreateDTO;
import java.util.ArrayList;
import java.util.List;

public class CategoriaGastoValidator {

    private CategoriaGastoValidator() {
    }

    public static List<String> validarCreacion(CategoriaGastoCreateDTO dto) {
        List<String> errores = new ArrayList<>();

        errores.addAll(ValidadorUtil.validarNombre(dto.getNombre(), "El nombre de la categoría"));
        
        if (dto.getTipo() == null) {
            errores.add("Debe seleccionar un tipo de categoría");
        }

        return errores;
    }
}
