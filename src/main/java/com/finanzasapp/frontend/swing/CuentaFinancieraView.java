package com.finanzasapp.frontend.swing;

import com.finanzasapp.backend.controller.CuentaFinancieraController;
import com.finanzasapp.backend.model.dto.CuentaFinancieraDTO;
import com.finanzasapp.backend.model.entity.Moneda;
import com.finanzasapp.backend.model.entity.TipoCuenta;
import com.finanzasapp.backend.repository.MonedaRepository;
import com.finanzasapp.backend.repository.TipoCuentaRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CuentaFinancieraView extends JPanel {

    private CuentaFinancieraController controller;
    private Long idUsuario;
    private MainView principal;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;

    public CuentaFinancieraView(CuentaFinancieraController controller, Long idUsuario) {
        this.controller = controller;
        this.idUsuario = idUsuario;
        initComponents();
        cargarCuentas();
    }

    public void setPrincipal(MainView principal) {
        this.principal = principal;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Gestión de Cuentas Financieras");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Tipo Cuenta", "Moneda", "Saldo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNuevo = new JButton("Nueva Cuenta");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        btnNuevo.addActionListener(e -> mostrarDialogoCrear());
        btnEditar.addActionListener(e -> mostrarDialogoEditar());
        btnEliminar.addActionListener(e -> eliminarCuenta());
        btnActualizar.addActionListener(e -> cargarCuentas());

        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnActualizar);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarCuentas() {
        modeloTabla.setRowCount(0);
        try {
            List<CuentaFinancieraDTO> cuentas = controller.listarPorUsuario(idUsuario);
            for (CuentaFinancieraDTO cuenta : cuentas) {
                modeloTabla.addRow(new Object[]{
                    cuenta.getId(),
                    cuenta.getNombre(),
                    cuenta.getNombreTipoCuenta(),
                    cuenta.getNombreMoneda() + " (" + cuenta.getSimboloMoneda() + ")",
                    cuenta.getSaldoActual(),
                    cuenta.getActivo() ? "Activa" : "Inactiva"
                });
            }
        } catch (Exception e) {
            mostrarError("Error al cargar cuentas: " + e.getMessage());
        }
    }

    private void mostrarDialogoCrear() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Cuenta", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField(20);
        JComboBox<TipoCuenta> cmbTipo = new JComboBox<>();
        JComboBox<Moneda> cmbMoneda = new JComboBox<>();

        try {
            List<TipoCuenta> tipos = getTipoCuentas();
            for (TipoCuenta t : tipos) {
                cmbTipo.addItem(t);
            }
            List<Moneda> monedas = getMonedas();
            for (Moneda m : monedas) {
                cmbMoneda.addItem(m);
            }
        } catch (Exception e) {
            mostrarError("Error al cargar datos: " + e.getMessage());
            return;
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Tipo Cuenta:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Moneda:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbMoneda, gbc);

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
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarError("El nombre es obligatorio");
                return;
            }
            if (cmbTipo.getSelectedItem() == null || cmbMoneda.getSelectedItem() == null) {
                mostrarError("Debe seleccionar tipo de cuenta y moneda");
                return;
            }

            try {
                TipoCuenta tipoSeleccionado = (TipoCuenta) cmbTipo.getSelectedItem();
                Moneda monedaSeleccionada = (Moneda) cmbMoneda.getSelectedItem();
                
                controller.crear(nombre, idUsuario, tipoSeleccionado.getId(), monedaSeleccionada.getId());
                cargarCuentas();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void mostrarDialogoEditar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione una cuenta para editar");
            return;
        }

        Long id = (Long) modeloTabla.getValueAt(fila, 0);
        CuentaFinancieraDTO cuenta = controller.buscarPorId(id);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Cuenta", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField(cuenta.getNombre(), 20);
        JComboBox<TipoCuenta> cmbTipo = new JComboBox<>();
        JComboBox<Moneda> cmbMoneda = new JComboBox<>();

        try {
            List<TipoCuenta> tipos = getTipoCuentas();
            for (TipoCuenta t : tipos) {
                cmbTipo.addItem(t);
                if (t.getId().equals(cuenta.getIdTipoCuenta())) {
                    cmbTipo.setSelectedItem(t);
                }
            }
            List<Moneda> monedas = getMonedas();
            for (Moneda m : monedas) {
                cmbMoneda.addItem(m);
                if (m.getId().equals(cuenta.getIdMoneda())) {
                    cmbMoneda.setSelectedItem(m);
                }
            }
        } catch (Exception e) {
            mostrarError("Error al cargar datos");
            return;
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Tipo Cuenta:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Moneda:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbMoneda, gbc);

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
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarError("El nombre es obligatorio");
                return;
            }

            try {
                TipoCuenta tipoSeleccionado = (TipoCuenta) cmbTipo.getSelectedItem();
                Moneda monedaSeleccionada = (Moneda) cmbMoneda.getSelectedItem();
                
                controller.actualizar(id, nombre, tipoSeleccionado.getId(), monedaSeleccionada.getId());
                cargarCuentas();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cuenta actualizada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void eliminarCuenta() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione una cuenta para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar esta cuenta?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) modeloTabla.getValueAt(fila, 0);
                controller.eliminar(id);
                cargarCuentas();
                JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                mostrarError(e.getMessage());
            }
        }
    }

    private List<TipoCuenta> getTipoCuentas() {
        return com.finanzasapp.database.JPAConfig.getEntityManager()
            .createQuery("SELECT t FROM TipoCuenta t WHERE t.activo = true", TipoCuenta.class)
            .getResultList();
    }

    private List<Moneda> getMonedas() {
        return com.finanzasapp.database.JPAConfig.getEntityManager()
            .createQuery("SELECT m FROM Moneda m WHERE m.activa = true", Moneda.class)
            .getResultList();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
