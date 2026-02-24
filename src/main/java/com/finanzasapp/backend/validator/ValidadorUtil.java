package com.finanzasapp.backend.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidadorUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[A-Za-z0-9_]{3,20}$");
    
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 50;
    private static final int MIN_NOMBRE_LENGTH = 2;
    private static final int MAX_NOMBRE_LENGTH = 50;
    private static final BigDecimal MAX_MONTO = new BigDecimal("999999999.99");
    private static final BigDecimal MIN_MONTO = BigDecimal.ZERO;

    private ValidadorUtil() {
    }

    public static List<String> validarCampoRequerido(String valor, String nombreCampo) {
        List<String> errores = new ArrayList<>();
        if (valor == null || valor.trim().isEmpty()) {
            errores.add(nombreCampo + " es obligatorio");
        }
        return errores;
    }

    public static List<String> validarMonto(BigDecimal monto, String nombreCampo) {
        List<String> errores = new ArrayList<>();
        
        if (monto == null) {
            errores.add(nombreCampo + " es obligatorio");
            return errores;
        }
        
        if (monto.compareTo(MIN_MONTO) <= 0) {
            errores.add(nombreCampo + " debe ser mayor a 0");
        }
        
        if (monto.compareTo(MAX_MONTO) > 0) {
            errores.add(nombreCampo + " excede el monto máximo permitido");
        }
        
        return errores;
    }

    public static List<String> validarMontoNoNegativo(BigDecimal monto, String nombreCampo) {
        List<String> errores = new ArrayList<>();
        
        if (monto == null) {
            errores.add(nombreCampo + " es obligatorio");
            return errores;
        }
        
        if (monto.compareTo(MIN_MONTO) < 0) {
            errores.add(nombreCampo + " no puede ser negativo");
        }
        
        return errores;
    }

    public static List<String> validarLongitud(String valor, String nombreCampo, int min, int max) {
        List<String> errores = new ArrayList<>();
        if (valor == null) {
            errores.add(nombreCampo + " es obligatorio");
            return errores;
        }
        
        int longitud = valor.trim().length();
        if (longitud < min) {
            errores.add(nombreCampo + " debe tener al menos " + min + " caracteres");
        }
        if (longitud > max) {
            errores.add(nombreCampo + " debe tener como máximo " + max + " caracteres");
        }
        return errores;
    }

    public static List<String> validarEmail(String email) {
        List<String> errores = new ArrayList<>();
        if (email == null || email.trim().isEmpty()) {
            return errores;
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errores.add("El formato del email no es válido");
        }
        return errores;
    }

    public static List<String> validarUsername(String username) {
        List<String> errores = new ArrayList<>();
        
        List<String> reqErrors = validarCampoRequerido(username, "Usuario");
        errores.addAll(reqErrors);
        
        if (reqErrors.isEmpty()) {
            if (username.length() < 3 || username.length() > 20) {
                errores.add("Usuario debe tener entre 3 y 20 caracteres");
            }
        }
        
        return errores;
    }

    public static List<String> validarPassword(String password) {
        List<String> errores = new ArrayList<>();
        
        List<String> reqErrors = validarCampoRequerido(password, "Contraseña");
        errores.addAll(reqErrors);
        
        if (reqErrors.isEmpty()) {
            int longitud = password.length();
            if (longitud < MIN_PASSWORD_LENGTH) {
                errores.add("Contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
            }
            if (longitud > MAX_PASSWORD_LENGTH) {
                errores.add("Contraseña debe tener como máximo " + MAX_PASSWORD_LENGTH + " caracteres");
            }
        }
        
        return errores;
    }

    public static List<String> validarNombre(String nombre, String tipo) {
        List<String> errores = new ArrayList<>();
        
        List<String> reqErrors = validarCampoRequerido(nombre, tipo);
        errores.addAll(reqErrors);
        
        if (reqErrors.isEmpty()) {
            errores.addAll(validarLongitud(nombre, tipo, MIN_NOMBRE_LENGTH, MAX_NOMBRE_LENGTH));
        }
        
        return errores;
    }

    public static List<String> validarId(Long id, String nombreCampo) {
        List<String> errores = new ArrayList<>();
        if (id == null || id <= 0) {
            errores.add("Debe seleccionar un " + nombreCampo + " válido");
        }
        return errores;
    }

    public static String obtenerMensajeError(List<String> errores) {
        if (errores == null || errores.isEmpty()) {
            return null;
        }
        return String.join("\n", errores);
    }
}
