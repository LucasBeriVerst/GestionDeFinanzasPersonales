package com.finanzasapp.frontend.swing;

import com.finanzasapp.backend.controller.LoginController;
import com.finanzasapp.backend.model.dto.UsuarioDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    private LoginController loginController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private MainView mainView;

    private static final Color COLOR_PRIMARY = new Color(52, 152, 219);
    private static final Color COLOR_BACKGROUND = new Color(236, 240, 241);
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_TEXT = new Color(44, 62, 80);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    public LoginView() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    private void initComponents() {
        setTitle("FinanzasApp - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        setExtendedState(JFrame.NORMAL);

        setLayout(new BorderLayout());
        setBackground(COLOR_BACKGROUND);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("💰 FinanzasApp", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(COLOR_TEXT);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblTitle, gbc);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(COLOR_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(8, 8, 8, 8);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;

        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.weightx = 0.3;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(FONT_LABEL);
        lblUsuario.setForeground(COLOR_TEXT);
        cardPanel.add(lblUsuario, cardGbc);

        cardGbc.gridx = 1;
        cardGbc.weightx = 0.7;
        txtUsername = new JTextField(20);
        txtUsername.setFont(FONT_LABEL);
        txtUsername.setPreferredSize(new Dimension(0, 40));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        cardPanel.add(txtUsername, cardGbc);

        cardGbc.gridx = 0;
        cardGbc.gridy = 1;
        cardGbc.weightx = 0.3;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(FONT_LABEL);
        lblPassword.setForeground(COLOR_TEXT);
        cardPanel.add(lblPassword, cardGbc);

        cardGbc.gridx = 1;
        cardGbc.weightx = 0.7;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(FONT_LABEL);
        txtPassword.setPreferredSize(new Dimension(0, 40));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        cardPanel.add(txtPassword, cardGbc);

        cardGbc.gridx = 0;
        cardGbc.gridy = 2;
        cardGbc.gridwidth = 2;
        cardGbc.insets = new Insets(20, 8, 8, 8);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        btnLogin = createButton("Iniciar Sesión", COLOR_PRIMARY);
        btnRegister = createButton("Registrarse", new Color(46, 204, 113));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        cardPanel.add(buttonPanel, cardGbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        mainPanel.add(cardPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> onLogin());
        txtPassword.addActionListener(e -> onLogin());
        btnRegister.addActionListener(e -> onRegister());
    }

    private JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(140, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            DialogosUI.mostrarError(this, "Por favor complete todos los campos");
            return;
        }

        try {
            UsuarioDTO usuario = loginController.login(username, password);
            dispose();
            mainView.setUsuarioActual(usuario);
            mainView.setVisible(true);
        } catch (Exception e) {
            DialogosUI.mostrarError(this, e.getMessage());
        }
    }

    private void onRegister() {
        showRegisterDialog();
    }

    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Registrarse", true);
        dialog.setMinimumSize(new Dimension(500, 450));
        dialog.setPreferredSize(new Dimension(550, 500));
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Crear Nueva Cuenta", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXT);
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_CARD);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(8, 8, 8, 8);
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNewUsername = createTextField();
        JPasswordField txtNewPassword = createPasswordField();
        JTextField txtEmail = createTextField();

        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.weightx = 0.3;
        JLabel lblUsuario = createLabel("Usuario:");
        formPanel.add(lblUsuario, formGbc);
        
        formGbc.gridx = 1;
        formGbc.weightx = 0.7;
        formPanel.add(txtNewUsername, formGbc);

        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formGbc.weightx = 0.3;
        JLabel lblPassword = createLabel("Contraseña:");
        formPanel.add(lblPassword, formGbc);
        
        formGbc.gridx = 1;
        formGbc.weightx = 0.7;
        formPanel.add(txtNewPassword, formGbc);

        formGbc.gridx = 0;
        formGbc.gridy = 2;
        formGbc.weightx = 0.3;
        JLabel lblEmail = createLabel("Correo:");
        formPanel.add(lblEmail, formGbc);
        
        formGbc.gridx = 1;
        formGbc.weightx = 0.7;
        formPanel.add(txtEmail, formGbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(formPanel, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setOpaque(false);
        
        JButton btnAceptar = createButton("Registrarse", COLOR_PRIMARY);
        JButton btnCancelar = createButton("Cancelar", new Color(149, 165, 166));
        
        btnPanel.add(btnAceptar);
        btnPanel.add(btnCancelar);

        gbc.gridwidth = 2;
        gbc.gridy = 2;
        mainPanel.add(btnPanel, gbc);

        dialog.add(mainPanel);

        btnAceptar.addActionListener(e -> {
            String username = txtNewUsername.getText().trim();
            String password = new String(txtNewPassword.getPassword());
            String email = txtEmail.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                DialogosUI.mostrarError(dialog, "Usuario y contraseña son obligatorios");
                return;
            }

            try {
                loginController.registrarUsuario(username, password, email.isEmpty() ? null : email);
                DialogosUI.mostrarExito(dialog, "Usuario registrado exitosamente");
                dialog.dispose();
            } catch (Exception ex) {
                DialogosUI.mostrarError(dialog, ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.pack();
        dialog.setVisible(true);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(FONT_LABEL);
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(FONT_LABEL);
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT);
        return label;
    }

    public void limpiarCampos() {
        txtUsername.setText("");
        txtPassword.setText("");
    }
}
