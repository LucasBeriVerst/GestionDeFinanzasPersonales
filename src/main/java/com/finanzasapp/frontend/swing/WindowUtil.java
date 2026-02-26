package com.finanzasapp.frontend.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowUtil {

    private static final Dimension MIN_SIZE = new Dimension(800, 600);

    private WindowUtil() {
    }

    public static void configurarVentanaPrincipal(JFrame frame) {
        frame.setMinimumSize(MIN_SIZE);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (DialogosUI.confirmar(frame, "¿Está seguro que desea salir de la aplicación?")) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }

    public static void configurarVentanaDialogo(JDialog dialog) {
        dialog.setMinimumSize(new Dimension(400, 300));
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });
    }

    public static void confirmarCierre(JFrame frame, Runnable onConfirm) {
        if (DialogosUI.confirmar(frame, "¿Está seguro que desea salir de la aplicación?")) {
            onConfirm.run();
        }
    }

    public static void confirmarCierre(JFrame frame) {
        confirmarCierre(frame, () -> {
            frame.dispose();
            System.exit(0);
        });
    }

    public static Dimension getMinSize() {
        return MIN_SIZE;
    }
}
