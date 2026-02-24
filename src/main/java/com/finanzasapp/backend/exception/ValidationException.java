package com.finanzasapp.backend.exception;

import java.util.List;

public class ValidationException extends BusinessException {
    
    private List<String> errores;

    public ValidationException(String mensaje) {
        super(mensaje, "VALIDATION_ERROR");
        this.errores = List.of(mensaje);
    }

    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, "VALIDATION_ERROR", causa);
        this.errores = List.of(mensaje);
    }
    
    public ValidationException(List<String> errores) {
        super(String.join("\n", errores), "VALIDATION_ERROR");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
    
    public boolean tieneErrores() {
        return errores != null && !errores.isEmpty();
    }
}
