package com.finanzasapp.frontend.swing;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.model.dto.*;
import com.finanzasapp.backend.repository.*;
import com.finanzasapp.backend.repository.impl.*;
import com.finanzasapp.backend.service.impl.*;
import com.finanzasapp.backend.service.interfaces.*;
import jakarta.persistence.EntityManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {

    private EntityManager em;
    private Long usuarioId;
    private IGastoService gastoService;
    private ICuentaFinancieraService cuentaService;
    
    private JLabel lblTotalGastos;
    private JLabel lblTotalCuentas;
    private JLabel lblNumCuentas;
    private JList<String> listaGastos;
    private JList<String> listaCuentas;
    private DefaultPieDataset pieDataset;
    private DefaultCategoryDataset barDataset;

    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);
        
        try {
            inicializarServicios();
            crearLayout();
            cargarDatos();
        } catch (Exception e) {
            JLabel error = new JLabel("Error: " + e.getMessage());
            add(error, BorderLayout.CENTER);
        }
    }

    private void inicializarServicios() throws Exception {
        em = JPAConfig.getEntityManager();
        
        UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl(em);
        List<com.finanzasapp.backend.model.entity.Usuario> usuarios = usuarioRepo.findByActivoTrue();
        
        if (!usuarios.isEmpty()) {
            usuarioId = usuarios.get(0).getId();
        }
        
        gastoService = new GastoServiceImpl(
            em, new GastoRepositoryImpl(em), new CuentaFinancieraRepositoryImpl(em),
            new UsuarioRepositoryImpl(em), new CategoriaGastoRepositoryImpl(em),
            new MonedaRepositoryImpl(em), new MovimientoRepositoryImpl(em));
        
        cuentaService = new CuentaFinancieraServiceImpl(
            new CuentaFinancieraRepositoryImpl(em), new UsuarioRepositoryImpl(em),
            new TipoCuentaRepositoryImpl(em), new MonedaRepositoryImpl(em));
    }

    private void crearLayout() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("💰 Dashboard Financiero");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarDatos());
        
        header.add(title, BorderLayout.WEST);
        header.add(btnActualizar, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
        
        // Centro
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        
        // Stats Cards
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setOpaque(false);
        
        lblTotalGastos = crearStatCard("Total Gastos", "$0.00", new Color(231, 76, 60));
        lblTotalCuentas = crearStatCard("Saldo Total", "$0.00", new Color(39, 174, 96));
        lblNumCuentas = crearStatCard("Cuentas Activas", "0", new Color(52, 152, 219));
        
        statsPanel.add(lblTotalGastos.getParent());
        statsPanel.add(lblTotalCuentas.getParent());
        statsPanel.add(lblNumCuentas.getParent());
        
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        
        // Charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        chartsPanel.setOpaque(false);
        
        // Pie Chart
        pieDataset = new DefaultPieDataset();
        JFreeChart pieChart = ChartFactory.createPieChart(
            "Gastos por Categoría", pieDataset, true, true, false);
        ChartPanel piePanel = new ChartPanel(pieChart);
        piePanel.setPreferredSize(new Dimension(400, 300));
        
        // Bar Chart
        barDataset = new DefaultCategoryDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
            "Gastos Mensuales", "Mes", "Monto", barDataset);
        ChartPanel barPanel = new ChartPanel(barChart);
        barPanel.setPreferredSize(new Dimension(400, 300));
        
        chartsPanel.add(piePanel);
        chartsPanel.add(barPanel);
        
        centerPanel.add(chartsPanel, BorderLayout.CENTER);
        
        // Listas inferiores
        JPanel listasPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        listasPanel.setOpaque(false);
        
        JPanel ultGastos = new JPanel(new BorderLayout());
        ultGastos.setBorder(BorderFactory.createTitledBorder("Últimos Gastos"));
        listaGastos = new JList<>();
        ultGastos.add(new JScrollPane(listaGastos), BorderLayout.CENTER);
        
        JPanel ultCuentas = new JPanel(new BorderLayout());
        ultCuentas.setBorder(BorderFactory.createTitledBorder("Cuentas Financieras"));
        listaCuentas = new JList<>();
        ultCuentas.add(new JScrollPane(listaCuentas), BorderLayout.CENTER);
        
        listasPanel.add(ultGastos);
        listasPanel.add(ultCuentas);
        
        centerPanel.add(listasPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel crearStatCard(String titulo, String valor, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setPreferredSize(new Dimension(200, 80));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(127, 140, 141));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 24));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(5));
        card.add(lblValor);
        
        return lblValor;
    }

    public void cargarDatos() {
        try {
            List<GastoDTO> gastos = gastoService.listarPorUsuario(usuarioId);
            
            BigDecimal totalGastos = gastos.stream()
                .map(GastoDTO::getMonto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            List<CuentaFinancieraDTO> cuentas = cuentaService.listarPorUsuario(usuarioId);
            
            BigDecimal saldoTotal = cuentas.stream()
                .map(CuentaFinancieraDTO::getSaldoActual)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            SwingUtilities.invokeLater(() -> {
                lblTotalGastos.setText("$" + totalGastos.setScale(2, java.math.RoundingMode.HALF_UP));
                lblTotalCuentas.setText("$" + saldoTotal.setScale(2, java.math.RoundingMode.HALF_UP));
                lblNumCuentas.setText(String.valueOf(cuentas.size()));
                
                // Pie chart
                pieDataset.clear();
                Map<String, BigDecimal> gastosPorCategoria = new HashMap<>();
                for (GastoDTO gasto : gastos) {
                    String categoria = gasto.getNombreCategoria() != null ? 
                        gasto.getNombreCategoria() : "Sin categoría";
                    BigDecimal monto = gasto.getMonto() != null ? gasto.getMonto() : BigDecimal.ZERO;
                    gastosPorCategoria.merge(categoria, monto, BigDecimal::add);
                }
                for (Map.Entry<String, BigDecimal> entry : gastosPorCategoria.entrySet()) {
                    pieDataset.setValue(entry.getKey(), entry.getValue().doubleValue());
                }
                
                // Bar chart
                barDataset.clear();
                String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun"};
                Random random = new Random();
                for (String mes : meses) {
                    barDataset.addValue(random.nextDouble() * 1000, "Gastos", mes);
                }
                
                // Listas
                DefaultListModel<String> modelGastos = new DefaultListModel<>();
                for (GastoDTO gasto : gastos.stream().limit(5).collect(Collectors.toList())) {
                    String item = String.format("%s - $%.2f - %s",
                        gasto.getDescripcion() != null ? gasto.getDescripcion() : "Sin descripción",
                        gasto.getMonto() != null ? gasto.getMonto() : BigDecimal.ZERO,
                        gasto.getNombreCategoria());
                    modelGastos.addElement(item);
                }
                listaGastos.setModel(modelGastos);
                
                DefaultListModel<String> modelCuentas = new DefaultListModel<>();
                for (CuentaFinancieraDTO cuenta : cuentas) {
                    BigDecimal saldo = cuenta.getSaldoActual() != null ? 
                        cuenta.getSaldoActual() : BigDecimal.ZERO;
                    String item = String.format("%s - Saldo: $%.2f", cuenta.getNombre(), saldo);
                    modelCuentas.addElement(item);
                }
                listaCuentas.setModel(modelCuentas);
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
