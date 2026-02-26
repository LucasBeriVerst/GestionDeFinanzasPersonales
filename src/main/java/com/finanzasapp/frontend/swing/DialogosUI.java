package com.finanzasapp.frontend.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogosUI {

    private static final Color COLOR_PRIMARY = new Color(52, 152, 219);
    private static final Color COLOR_ERROR = new Color(231, 76, 60);
    private static final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private static final Color COLOR_WARNING = new Color(241, 196, 15);
    private static final Color COLOR_BACKGROUND = new Color(236, 240, 241);
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_TEXT = new Color(44, 62, 80);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_MESSAGE = new Font("Segoe UI", Font.PLAIN, 14);

    private DialogosUI() {
    }

    public static void mostrarError(Component parent, String mensaje) {
        mostrarMensaje(parent, "Error", mensaje, COLOR_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarAdvertencia(Component parent, String mensaje) {
        mostrarMensaje(parent, "Advertencia", mensaje, COLOR_WARNING, JOptionPane.WARNING_MESSAGE);
    }

    public static void mostrarInfo(Component parent, String mensaje) {
        mostrarMensaje(parent, "Información", mensaje, COLOR_PRIMARY, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarExito(Component parent, String mensaje) {
        mostrarMensaje(parent, "Éxito", mensaje, COLOR_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component parent, String mensaje) {
        return mostrarConfirmacion(parent, "Confirmar", mensaje);
    }

    private static void mostrarMensaje(Component parent, String titulo, String mensaje, Color colorBoton, int tipoMensaje) {
        int lineas = mensaje.split("\n").length;
        int altura = 180 + (lineas - 1) * 25;
        altura = Math.min(altura, 400);
        
        JDialog dialog = crearDialogo(parent, titulo, 420, altura);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(COLOR_CARD);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 30, 30), 3),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, colorBoton),
                new EmptyBorder(20, 20, 20, 20)
            )
        ));

        JLabel lblIcono = new JLabel(getIcono(tipoMensaje));
        lblIcono.setPreferredSize(new Dimension(50, 50));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new BorderLayout(5, 5));
        textPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(COLOR_TEXT);
        
        String mensajeFormateado = mensaje.replace("\n", "<br/>- ");
        if (!mensajeFormateado.startsWith("<br/>")) {
            mensajeFormateado = "- " + mensajeFormateado;
        }
        JLabel lblMensaje = new JLabel("<html><body>" + mensajeFormateado + "</body></html>");
        lblMensaje.setFont(FONT_MESSAGE);
        lblMensaje.setForeground(COLOR_TEXT);
        
        textPanel.add(lblTitulo, BorderLayout.NORTH);
        textPanel.add(lblMensaje, BorderLayout.CENTER);
        
        contentPanel.add(lblIcono, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        JButton btnAceptar = crearBoton("Aceptar", colorBoton);
        btnAceptar.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(btnAceptar);
        
        contentPanel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static boolean mostrarConfirmacion(Component parent, String titulo, String mensaje) {
        JDialog dialog = crearDialogo(parent, titulo, 420, 200);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(COLOR_CARD);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 30, 30), 3),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblIcono = new JLabel("❓");
        lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        lblIcono.setPreferredSize(new Dimension(50, 50));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new BorderLayout(5, 5));
        textPanel.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(COLOR_TEXT);
        
        JLabel lblMensaje = new JLabel("<html><body>" + mensaje + "</body></html>");
        lblMensaje.setFont(FONT_MESSAGE);
        lblMensaje.setForeground(COLOR_TEXT);
        
        textPanel.add(lblTitulo, BorderLayout.NORTH);
        textPanel.add(lblMensaje, BorderLayout.CENTER);
        
        contentPanel.add(lblIcono, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        final boolean[] resultado = {false};
        
        JButton btnSi = crearBoton("Sí", COLOR_SUCCESS);
        btnSi.addActionListener(e -> {
            resultado[0] = true;
            dialog.dispose();
        });
        
        JButton btnNo = crearBoton("No", new Color(149, 165, 166));
        btnNo.addActionListener(e -> {
            resultado[0] = false;
            dialog.dispose();
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnSi);
        btnPanel.add(btnNo);
        
        contentPanel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        
        return resultado[0];
    }

    private static JDialog crearDialogo(Component parent, String titulo, int ancho, int alto) {
        JDialog dialog = new JDialog(
            (parent instanceof Frame) ? (Frame) parent : null,
            titulo,
            true
        );
        dialog.setSize(ancho, alto);
        dialog.setMinimumSize(new Dimension(ancho, alto));
        dialog.setMaximumSize(new Dimension(ancho, alto));
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        return dialog;
    }

    private static JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(100, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
        
        return boton;
    }

    private static String getIcono(int tipo) {
        switch (tipo) {
            case JOptionPane.ERROR_MESSAGE: return "❌";
            case JOptionPane.WARNING_MESSAGE: return "⚠️";
            case JOptionPane.INFORMATION_MESSAGE: return "ℹ️";
            case JOptionPane.QUESTION_MESSAGE: return "❓";
            default: return "ℹ️";
        }
    }
}
