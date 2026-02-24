package com.finanzasapp.exception;

public class BusinessException extends RuntimeException {
    
    private String codigoError;

    public BusinessException(String mensaje) {
        super(mensaje);
    }

    public BusinessException(String mensaje, String codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public BusinessException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public BusinessException(String mensaje, String codigoError, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}
