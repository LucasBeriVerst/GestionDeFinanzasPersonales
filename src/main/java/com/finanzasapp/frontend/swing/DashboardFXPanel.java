package com.finanzasapp.frontend.swing;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.model.dto.*;
import com.finanzasapp.backend.repository.*;
import com.finanzasapp.backend.repository.impl.*;
import com.finanzasapp.backend.service.impl.*;
import com.finanzasapp.backend.service.interfaces.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardFXPanel extends JPanel {

    private EntityManager em;
    private Long usuarioId;
    private IGastoService gastoService;
    private ICuentaFinancieraService cuentaService;
    private JFXPanel jfxPanel;
    private VBox root;

    public DashboardFXPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        try {
            inicializarServicios();
            inicializarJavaFX();
        } catch (Exception e) {
            add(new JLabel("Error: " + e.getMessage()), BorderLayout.CENTER);
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

    private void inicializarJavaFX() {
        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);
        
        Platform.runLater(() -> {
            root = new VBox(20);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #f5f5f5;");
            
            // Header
            HBox header = new HBox();
            header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 15;");
            
            Label title = new Label("💰 Dashboard Financiero");
            title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: white;");
            
            Button btnActualizar = new Button("🔄 Actualizar");
            btnActualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 5 10;");
            btnActualizar.setOnAction(e -> cargarDatosFX());
            
            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            header.getChildren().addAll(title, spacer, btnActualizar);
            
            // Stats
            HBox stats = new HBox(20);
            
            VBox card1 = crearStatCard("Total Gastos", "$0.00", "#e74c3c");
            VBox card2 = crearStatCard("Saldo Total", "$0.00", "#27ae60");
            VBox card3 = crearStatCard("Cuentas", "0", "#3498db");
            
            stats.getChildren().addAll(card1, card2, card3);
            
            // Charts
            HBox charts = new HBox(20);
            
            PieChart pieChart = new PieChart();
            pieChart.setPrefSize(380, 280);
            
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setPrefSize(380, 280);
            
            charts.getChildren().addAll(pieChart, barChart);
            
            root.getChildren().addAll(header, stats, charts);
            
            Scene scene = new Scene(root, 900, 500);
            jfxPanel.setScene(scene);
            
            cargarDatosFX();
        });
    }

    private VBox crearStatCard(String titulo, String valor, String color) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10;");
        card.setPrefWidth(250);
        card.setAlignment(Pos.CENTER);
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 12; -fx-text-fill: #7f8c8d;");
        
        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        lblValor.setId("lblValor");
        
        card.getChildren().addAll(lblTitulo, lblValor);
        
        return card;
    }

    private void cargarDatosFX() {
        if (root == null) return;
        
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
            
            Platform.runLater(() -> {
                // Actualizar stats (buscar por ID)
                for (javafx.scene.Node node : ((HBox)root.getChildren().get(1)).getChildren()) {
                    if (node instanceof VBox) {
                        VBox card = (VBox) node;
                        Label valor = (Label) card.lookup("#lblValor");
                        if (valor != null) {
                            if (card.getChildren().get(0) instanceof Label) {
                                Label titulo = (Label) card.getChildren().get(0);
                                String text = titulo.getText();
                                if (text.contains("Gastos")) {
                                    valor.setText("$" + totalGastos.setScale(2, java.math.RoundingMode.HALF_UP));
                                } else if (text.contains("Saldo")) {
                                    valor.setText("$" + saldoTotal.setScale(2, java.math.RoundingMode.HALF_UP));
                                } else if (text.contains("Cuentas")) {
                                    valor.setText(String.valueOf(cuentas.size()));
                                }
                            }
                        }
                    }
                }
                
                // Pie chart
                PieChart pie = (PieChart) ((HBox)root.getChildren().get(2)).getChildren().get(0);
                pie.getData().clear();
                Map<String, BigDecimal> gastosPorCategoria = new HashMap<>();
                for (GastoDTO gasto : gastos) {
                    String cat = gasto.getNombreCategoria() != null ? gasto.getNombreCategoria() : "Sin categoría";
                    BigDecimal monto = gasto.getMonto() != null ? gasto.getMonto() : BigDecimal.ZERO;
                    gastosPorCategoria.merge(cat, monto, BigDecimal::add);
                }
                for (Map.Entry<String, BigDecimal> e : gastosPorCategoria.entrySet()) {
                    pie.getData().add(new PieChart.Data(e.getKey(), e.getValue().doubleValue()));
                }
                
                // Bar chart
                BarChart<String, Number> bar = (BarChart<String, Number>) ((HBox)root.getChildren().get(2)).getChildren().get(1);
                bar.getData().clear();
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Gastos");
                String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun"};
                Random random = new Random();
                for (String mes : meses) {
                    series.getData().add(new XYChart.Data<>(mes, random.nextDouble() * 500 + 100));
                }
                bar.getData().add(series);
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
