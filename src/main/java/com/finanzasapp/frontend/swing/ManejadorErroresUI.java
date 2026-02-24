package com.finanzasapp.frontend.swing;

import com.finanzasapp.backend.exception.BusinessException;
import com.finanzasapp.backend.exception.ValidationException;
import javax.swing.*;

public class ManejadorErroresUI {

    private ManejadorErroresUI() {
    }

    public static void mostrarError(Exception e, JComponent parent) {
        String mensaje;
        
        if (e instanceof ValidationException) {
            ValidationException ve = (ValidationException) e;
            if (ve.tieneErrores()) {
                mensaje = String.join("\n• ", ve.getErrores());
                mensaje = "• " + mensaje;
            } else {
                mensaje = e.getMessage();
            }
        } else if (e instanceof BusinessException) {
            mensaje = e.getMessage();
        } else {
            mensaje = "Error inesperado: " + e.getMessage();
        }
        
        JOptionPane.showMessageDialog(
            parent, 
            mensaje, 
            "Error de Validación", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public static void mostrarError(String mensaje, JComponent parent) {
        JOptionPane.showMessageDialog(
            parent, 
            mensaje, 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public static void mostrarAdvertencia(String mensaje, JComponent parent) {
        JOptionPane.showMessageDialog(
            parent, 
            mensaje, 
            "Advertencia", 
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    public static void mostrarExito(String mensaje, JComponent parent) {
        JOptionPane.showMessageDialog(
            parent, 
            mensaje, 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static boolean confirmar(String mensaje, JComponent parent) {
        int respuesta = JOptionPane.showConfirmDialog(
            parent, 
            mensaje, 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION
        );
        return respuesta == JOptionPane.YES_OPTION;
    }
}
