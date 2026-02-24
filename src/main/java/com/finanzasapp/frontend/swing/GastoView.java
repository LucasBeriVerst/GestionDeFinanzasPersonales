package com.finanzasapp.frontend.swing;

import com.finanzasapp.backend.controller.CategoriaGastoController;
import com.finanzasapp.backend.controller.CuentaFinancieraController;
import com.finanzasapp.backend.controller.GastoController;
import com.finanzasapp.backend.model.dto.CategoriaGastoDTO;
import com.finanzasapp.backend.model.dto.CuentaFinancieraDTO;
import com.finanzasapp.backend.model.dto.GastoDTO;
import com.finanzasapp.backend.model.entity.Moneda;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class GastoView extends JPanel {

    private GastoController gastoController;
    private CuentaFinancieraController cuentaController;
    private CategoriaGastoController categoriaController;
    private Long idUsuario;
    private MainView principal;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JLabel lblTotalGastos;

    public GastoView(GastoController gastoController, CuentaFinancieraController cuentaController,
                     CategoriaGastoController categoriaController, Long idUsuario) {
        this.gastoController = gastoController;
        this.cuentaController = cuentaController;
        this.categoriaController = categoriaController;
        this.idUsuario = idUsuario;
        initComponents();
        cargarGastos();
    }

    public void setPrincipal(MainView principal) {
        this.principal = principal;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitulo = new JLabel("Gestión de Gastos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        lblTotalGastos = new JLabel("Total: $0.00");
        lblTotalGastos.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalGastos.setHorizontalAlignment(SwingConstants.RIGHT);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(lblTotalGastos, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columnas = {"ID", "Fecha", "Cuenta", "Categoría", "Monto", "Descripción"};
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
        btnNuevo = new JButton("Nuevo Gasto");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        btnNuevo.addActionListener(e -> mostrarDialogoCrear());
        btnEliminar.addActionListener(e -> eliminarGasto());
        btnActualizar.addActionListener(e -> {
            cargarGastos();
            if (principal != null) {
                principal.actualizarVista("CUENTAS");
            }
        });

        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnActualizar);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarGastos() {
        modeloTabla.setRowCount(0);
        BigDecimal total = BigDecimal.ZERO;
        
        try {
            List<GastoDTO> gastos = gastoController.listarPorUsuario(idUsuario);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (GastoDTO gasto : gastos) {
                modeloTabla.addRow(new Object[]{
                    gasto.getId(),
                    gasto.getFecha() != null ? sdf.format(java.sql.Timestamp.valueOf(gasto.getFecha())) : "-",
                    gasto.getNombreCuenta(),
                    gasto.getNombreCategoria(),
                    gasto.getMonto(),
                    gasto.getDescripcion() != null ? gasto.getDescripcion() : "-"
                });
                total = total.add(gasto.getMonto());
            }
            lblTotalGastos.setText("Total: " + total.toString());
        } catch (Exception e) {
            mostrarError("Error al cargar gastos: " + e.getMessage());
        }
    }

    private void mostrarDialogoCrear() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Gasto", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<CuentaFinancieraDTO> cuentas = cuentaController.listarPorUsuario(idUsuario);
        List<CategoriaGastoDTO> categorias = categoriaController.listarActivos();

        if (cuentas.isEmpty()) {
            mostrarError("Debe crear al menos una cuenta financiera primero");
            return;
        }
        if (categorias.isEmpty()) {
            mostrarError("Debe crear al menos una categoría de gasto primero");
            return;
        }

        JComboBox<CuentaFinancieraDTO> cmbCuenta = new JComboBox<>();
        for (CuentaFinancieraDTO c : cuentas) {
            cmbCuenta.addItem(c);
        }

        JComboBox<CategoriaGastoDTO> cmbCategoria = new JComboBox<>();
        for (CategoriaGastoDTO c : categorias) {
            cmbCategoria.addItem(c);
        }

        JTextField txtMonto = new JTextField(20);
        JTextField txtDescripcion = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Cuenta:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbCuenta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Monto:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtMonto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtDescripcion, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        btnPanel.add(btnAceptar);
        btnPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        JLabel lblInfo = new JLabel("El gasto descontará el monto del saldo de la cuenta");
        lblInfo.setForeground(Color.BLUE);
        gbc.gridy = 5;
        panel.add(lblInfo, gbc);

        dialog.add(panel);

        btnAceptar.addActionListener(e -> {
            BigDecimal monto;
            try {
                monto = new BigDecimal(txtMonto.getText().trim());
            } catch (NumberFormatException ex) {
                mostrarError("Ingrese un monto válido");
                return;
            }

            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarError("El monto debe ser mayor a 0");
                return;
            }

            try {
                CuentaFinancieraDTO cuentaSeleccionada = (CuentaFinancieraDTO) cmbCuenta.getSelectedItem();
                CategoriaGastoDTO categoriaSeleccionada = (CategoriaGastoDTO) cmbCategoria.getSelectedItem();
                String descripcion = txtDescripcion.getText().trim();

                gastoController.crear(
                    idUsuario,
                    cuentaSeleccionada.getId(),
                    categoriaSeleccionada.getId(),
                    monto,
                    descripcion.isEmpty() ? null : descripcion,
                    null
                );

                cargarGastos();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this, 
                    "Gasto registrado exitosamente.\nEl monto ha sido descontado de la cuenta.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

                if (principal != null) {
                    principal.actualizarVista("CUENTAS");
                }
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void eliminarGasto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione un gasto para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este gasto?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) modeloTabla.getValueAt(fila, 0);
                gastoController.eliminar(id);
                cargarGastos();
                
                if (principal != null) {
                    principal.actualizarVista("CUENTAS");
                }
                
                JOptionPane.showMessageDialog(this, "Gasto eliminado correctamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                mostrarError(e.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
