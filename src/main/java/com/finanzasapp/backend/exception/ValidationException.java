package com.finanzasapp.exception;

public class ValidationException extends BusinessException {

    public ValidationException(String mensaje) {
        super(mensaje, "VALIDATION_ERROR");
    }

    public ValidationException(String mensaje, Throwable causa) {
        super(mensaje, "VALIDATION_ERROR", causa);
    }
}
