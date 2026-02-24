package com.finanzasapp.view.swing;

import com.finanzasapp.controller.LoginController;
import com.finanzasapp.model.dto.UsuarioDTO;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private LoginController loginController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private MainView mainView;

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
        setSize(400, 300);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Gestión Financiera Personal", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtUsername = new JTextField(20);
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        formPanel.add(txtPassword, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnLogin = new JButton("Iniciar Sesión");
        btnRegister = new JButton("Registrarse");
        
        btnLogin.setPreferredSize(new Dimension(120, 30));
        btnRegister.setPreferredSize(new Dimension(120, 30));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnLogin.addActionListener(e -> onLogin());
        txtPassword.addActionListener(e -> onLogin());
        btnRegister.addActionListener(e -> onRegister());
    }

    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor complete todos los campos");
            return;
        }

        try {
            UsuarioDTO usuario = loginController.login(username, password);
            dispose();
            mainView.setUsuarioActual(usuario);
            mainView.setVisible(true);
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void onRegister() {
        showRegisterDialog();
    }

    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Registrarse", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNewUsername = new JTextField(20);
        JPasswordField txtNewPassword = new JPasswordField(20);
        JTextField txtEmail = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNewUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNewPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        btnPanel.add(btnAceptar);
        btnPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        dialog.add(panel);

        btnAceptar.addActionListener(e -> {
            String username = txtNewUsername.getText().trim();
            String password = new String(txtNewPassword.getPassword());
            String email = txtEmail.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                mostrarError("Usuario y contraseña son obligatorios");
                return;
            }

            try {
                loginController.registrarUsuario(username, password, email.isEmpty() ? null : email);
                JOptionPane.showMessageDialog(dialog, "Usuario registrado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarCampos() {
        txtUsername.setText("");
        txtPassword.setText("");
    }
}
