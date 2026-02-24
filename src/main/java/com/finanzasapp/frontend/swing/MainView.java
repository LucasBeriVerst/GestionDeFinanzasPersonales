package com.finanzasapp.view.swing;

import com.finanzasapp.controller.CategoriaGastoController;
import com.finanzasapp.controller.CuentaFinancieraController;
import com.finanzasapp.controller.GastoController;
import com.finanzasapp.controller.LoginController;
import com.finanzasapp.model.dto.UsuarioDTO;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private LoginController loginController;
    private CuentaFinancieraController cuentaController;
    private CategoriaGastoController categoriaController;
    private GastoController gastoController;
    private UsuarioDTO usuarioActual;

    private JLabel lblUsuario;
    private JPanel contentPanel;

    public MainView() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public void setControllers(LoginController loginController, 
                              CuentaFinancieraController cuentaController,
                              CategoriaGastoController categoriaController,
                              GastoController gastoController) {
        this.loginController = loginController;
        this.cuentaController = cuentaController;
        this.categoriaController = categoriaController;
        this.gastoController = gastoController;
    }

    public void setUsuarioActual(UsuarioDTO usuario) {
        this.usuarioActual = usuario;
        lblUsuario.setText("Usuario: " + usuario.getUsername());
    }

    public UsuarioDTO getUsuarioActual() {
        return usuarioActual;
    }

    private void initComponents() {
        setTitle("FinanzasApp - Gestión Financiera");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(true);

        setJMenuBar(createMenuBar());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("Gestión Financiera Personal", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        
        lblUsuario = new JLabel("Usuario: -", SwingConstants.RIGHT);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        
        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblUsuario, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new JPanel(), "VACIO");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JButton btnCuentas = new JButton("Cuentas");
        JButton btnCategorias = new JButton("Categorías");
        JButton btnGastos = new JButton("Gastos");
        JButton btnCerrar = new JButton("Cerrar Sesión");
        
        btnCuentas.addActionListener(e -> mostrarCuentas());
        btnCategorias.addActionListener(e -> mostrarCategorias());
        btnGastos.addActionListener(e -> mostrarGastos());
        btnCerrar.addActionListener(e -> cerrarSesion());
        
        buttonPanel.add(btnCuentas);
        buttonPanel.add(btnCategorias);
        buttonPanel.add(btnGastos);
        buttonPanel.add(btnCerrar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        
        JMenu menuCuentas = new JMenu("Cuentas");
        JMenuItem itemNuevaCuenta = new JMenuItem("Nueva Cuenta");
        itemNuevaCuenta.addActionListener(e -> mostrarCuentas());
        menuCuentas.add(itemNuevaCuenta);
        
        JMenu menuCategorias = new JMenu("Categorías");
        JMenuItem itemNuevaCategoria = new JMenuItem("Nueva Categoría");
        itemNuevaCategoria.addActionListener(e -> mostrarCategorias());
        menuCategorias.add(itemNuevaCategoria);
        
        JMenu menuGastos = new JMenu("Gastos");
        JMenuItem itemNuevoGasto = new JMenuItem("Nuevo Gasto");
        itemNuevoGasto.addActionListener(e -> mostrarGastos());
        menuGastos.add(itemNuevoGasto);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuCuentas);
        menuBar.add(menuCategorias);
        menuBar.add(menuGastos);
        
        return menuBar;
    }

    private void mostrarCuentas() {
        contentPanel.removeAll();
        CuentaFinancieraView vista = new CuentaFinancieraView(cuentaController, usuarioActual.getId());
        vista.setPrincipal(this);
        contentPanel.add(vista, "CUENTAS");
        contentPanel.revalidate();
        contentPanel.repaint();
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "CUENTAS");
    }

    private void mostrarCategorias() {
        contentPanel.removeAll();
        CategoriaGastoView vista = new CategoriaGastoView(categoriaController);
        vista.setPrincipal(this);
        contentPanel.add(vista, "CATEGORIAS");
        contentPanel.revalidate();
        contentPanel.repaint();
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "CATEGORIAS");
    }

    private void mostrarGastos() {
        contentPanel.removeAll();
        GastoView vista = new GastoView(gastoController, cuentaController, categoriaController, usuarioActual.getId());
        vista.setPrincipal(this);
        contentPanel.add(vista, "GASTOS");
        contentPanel.revalidate();
        contentPanel.repaint();
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "GASTOS");
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cerrar sesión?", 
            "Cerrar Sesión", 
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            loginController.logout();
            dispose();
            LoginView login = new LoginView();
            MainView main = new MainView();
            main.setVisible(true);
        }
    }

    public void actualizarVista(String vista) {
        switch (vista) {
            case "CUENTAS":
                mostrarCuentas();
                break;
            case "CATEGORIAS":
                mostrarCategorias();
                break;
            case "GASTOS":
                mostrarGastos();
                break;
        }
    }
}
