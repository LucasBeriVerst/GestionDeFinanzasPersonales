package com.finanzasapp.view.swing;

import com.finanzasapp.controller.CategoriaGastoController;
import com.finanzasapp.model.dto.CategoriaGastoDTO;
import com.finanzasapp.model.enums.TipoCategoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaGastoView extends JPanel {

    private CategoriaGastoController controller;
    private MainView principal;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;

    public CategoriaGastoView(CategoriaGastoController controller) {
        this.controller = controller;
        initComponents();
        cargarCategorias();
    }

    public void setPrincipal(MainView principal) {
        this.principal = principal;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Gestión de Categorías de Gastos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Descripción", "Tipo", "Estado"};
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
        btnNuevo = new JButton("Nueva Categoría");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        btnNuevo.addActionListener(e -> mostrarDialogoCrear());
        btnEditar.addActionListener(e -> mostrarDialogoEditar());
        btnEliminar.addActionListener(e -> eliminarCategoria());
        btnActualizar.addActionListener(e -> cargarCategorias());

        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnActualizar);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarCategorias() {
        modeloTabla.setRowCount(0);
        try {
            List<CategoriaGastoDTO> categorias = controller.listarActivos();
            for (CategoriaGastoDTO cat : categorias) {
                modeloTabla.addRow(new Object[]{
                    cat.getId(),
                    cat.getNombre(),
                    cat.getDescripcion() != null ? cat.getDescripcion() : "-",
                    cat.getTipo() != null ? cat.getTipo().name() : "-",
                    cat.getActivo() ? "Activa" : "Inactiva"
                });
            }
        } catch (Exception e) {
            mostrarError("Error al cargar categorías: " + e.getMessage());
        }
    }

    private void mostrarDialogoCrear() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Categoría", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField(20);
        JTextField txtDescripcion = new JTextField(20);
        JComboBox<TipoCategoria> cmbTipo = new JComboBox<>(TipoCategoria.values());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbTipo, gbc);

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
                String descripcion = txtDescripcion.getText().trim();
                TipoCategoria tipo = (TipoCategoria) cmbTipo.getSelectedItem();
                
                controller.crear(nombre, descripcion.isEmpty() ? null : descripcion, null, tipo);
                cargarCategorias();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Categoría creada exitosamente", 
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
            mostrarError("Seleccione una categoría para editar");
            return;
        }

        Long id = (Long) modeloTabla.getValueAt(fila, 0);
        CategoriaGastoDTO categoria = controller.buscarPorId(id);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Categoría", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField(categoria.getNombre(), 20);
        JTextField txtDescripcion = new JTextField(categoria.getDescripcion() != null ? categoria.getDescripcion() : "", 20);
        JComboBox<TipoCategoria> cmbTipo = new JComboBox<>(TipoCategoria.values());
        if (categoria.getTipo() != null) {
            cmbTipo.setSelectedItem(categoria.getTipo());
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
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cmbTipo, gbc);

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
                String descripcion = txtDescripcion.getText().trim();
                TipoCategoria tipo = (TipoCategoria) cmbTipo.getSelectedItem();
                
                controller.actualizar(id, nombre, descripcion.isEmpty() ? null : descripcion, null, tipo);
                cargarCategorias();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Categoría actualizada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void eliminarCategoria() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione una categoría para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar esta categoría?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) modeloTabla.getValueAt(fila, 0);
                controller.eliminar(id);
                cargarCategorias();
                JOptionPane.showMessageDialog(this, "Categoría eliminada correctamente", 
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
