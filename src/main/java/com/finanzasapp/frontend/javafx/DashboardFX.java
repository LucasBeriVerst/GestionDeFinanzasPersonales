package com.finanzasapp.frontend.javafx;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.model.dto.*;
import com.finanzasapp.backend.model.enums.TipoCategoria;
import com.finanzasapp.backend.repository.*;
import com.finanzasapp.backend.repository.impl.*;
import com.finanzasapp.backend.service.impl.*;
import com.finanzasapp.backend.service.interfaces.*;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardFX extends Application {

    private EntityManager em;
    private Long usuarioId;
    private IGastoService gastoService;
    private ICuentaFinancieraService cuentaService;
    
    private Label lblTotalGastos;
    private Label lblTotalCuentas;
    private Label lblNumCuentas;
    private PieChart chartGastos;
    private BarChart<String, Number> chartMensual;
    private ListView<String> listaGastos;
    private ListView<String> listaCuentas;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            em = JPAConfig.getEntityManager();
            
            // Inicializar servicios
            gastoService = new GastoServiceImpl(
                em, new GastoRepositoryImpl(em), new CuentaFinancieraRepositoryImpl(em),
                new UsuarioRepositoryImpl(em), new CategoriaGastoRepositoryImpl(em),
                new MonedaRepositoryImpl(em), new MovimientoRepositoryImpl(em));
            
            cuentaService = new CuentaFinancieraServiceImpl(
                new CuentaFinancieraRepositoryImpl(em), new UsuarioRepositoryImpl(em),
                new TipoCuentaRepositoryImpl(em), new MonedaRepositoryImpl(em));
            
            // Obtener usuario logueado (el primero por ahora)
            UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl(em);
            List<com.finanzasapp.backend.model.entity.Usuario> usuarios = usuarioRepo.findByActivoTrue();
            
            if (usuarios.isEmpty()) {
                showAlert("No hay usuarios activos", "Por favor cree un usuario primero en la aplicación Swing");
                System.exit(0);
                return;
            }
            
            usuarioId = usuarios.get(0).getId();
            
            stage.setTitle("Dashboard Financiero - JavaFX");
            stage.setWidth(1000);
            stage.setHeight(700);
            stage.initStyle(StageStyle.DECORATED);
            
            Scene scene = new Scene(createMainLayout(), 980, 660);
            
            stage.setScene(scene);
            stage.show();
            
            cargarDatos();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al cargar dashboard: " + e.getMessage());
        }
    }

    private VBox createMainLayout() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        HBox header = createHeader();
        HBox statsCards = createStatsCards();
        HBox charts = createCharts();
        HBox bottomInfo = createBottomInfo();

        mainLayout.getChildren().addAll(header, statsCards, charts, bottomInfo);
        
        return mainLayout;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 15;");
        header.setPrefHeight(60);
        
        Label title = new Label("💰 Dashboard Financiero");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Button btnActualizar = new Button("🔄 Actualizar");
        btnActualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15;");
        btnActualizar.setOnAction(e -> cargarDatos());
        
        Button btnCerrar = new Button("✕ Cerrar");
        btnCerrar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 15;");
        btnCerrar.setOnAction(e -> {
            if (em != null && em.isOpen()) em.close();
            System.exit(0);
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        header.getChildren().addAll(title, spacer, btnActualizar, btnCerrar);
        
        return header;
    }

    private HBox createStatsCards() {
        HBox stats = new HBox(20);
        
        lblTotalGastos = createStatCard("Total Gastos", "$0.00", "#e74c3c");
        lblTotalCuentas = createStatCard("Saldo Total", "$0.00", "#27ae60");
        lblNumCuentas = createStatCard("Cuentas Activas", "0", "#3498db");
        
        stats.getChildren().addAll(lblTotalGastos, lblTotalCuentas, lblNumCuentas);
        
        return stats;
    }

    private Label createStatCard(String titulo, String valor, String color) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        card.setPrefWidth(280);
        card.setAlignment(Pos.CENTER);
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 14; -fx-text-fill: #7f8c8d;");
        
        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        
        card.getChildren().addAll(lblTitulo, lblValor);
        
        return lblValor;
    }

    private HBox createCharts() {
        HBox charts = new HBox(20);
        
        VBox pieBox = new VBox(10);
        pieBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10;");
        pieBox.setPrefWidth(450);
        
        Label lblPie = new Label("Gastos por Categoría");
        lblPie.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        chartGastos = new PieChart();
        chartGastos.setPrefHeight(300);
        
        pieBox.getChildren().addAll(lblPie, chartGastos);
        
        VBox barBox = new VBox(10);
        barBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10;");
        barBox.setPrefWidth(480);
        
        Label lblBar = new Label("Gastos Mensuales");
        lblBar.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        chartMensual = new BarChart<>(xAxis, yAxis);
        chartMensual.setPrefHeight(300);
        
        barBox.getChildren().addAll(lblBar, chartMensual);
        
        charts.getChildren().addAll(pieBox, barBox);
        
        return charts;
    }

    private HBox createBottomInfo() {
        HBox bottom = new HBox(20);
        
        VBox ultGastos = new VBox(10);
        ultGastos.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10;");
        ultGastos.setPrefWidth(480);
        
        Label lblUltGastos = new Label("Últimos Gastos");
        lblUltGastos.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        listaGastos = new ListView<>();
        listaGastos.setPrefHeight(150);
        
        ultGastos.getChildren().addAll(lblUltGastos, listaGastos);
        
        VBox ultCuentas = new VBox(10);
        ultCuentas.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10;");
        ultCuentas.setPrefWidth(480);
        
        Label lblCuentas = new Label("Cuentas Financieras");
        lblCuentas.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        listaCuentas = new ListView<>();
        listaCuentas.setPrefHeight(150);
        
        ultCuentas.getChildren().addAll(lblCuentas, listaCuentas);
        
        bottom.getChildren().addAll(ultGastos, ultCuentas);
        
        return bottom;
    }

    private void cargarDatos() {
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
            
            lblTotalGastos.setText("$" + totalGastos.setScale(2, java.math.RoundingMode.HALF_UP));
            lblTotalCuentas.setText("$" + saldoTotal.setScale(2, java.math.RoundingMode.HALF_UP));
            lblNumCuentas.setText(String.valueOf(cuentas.size()));
            
            Map<String, BigDecimal> gastosPorCategoria = new HashMap<>();
            for (GastoDTO gasto : gastos) {
                String categoria = gasto.getNombreCategoria() != null ? gasto.getNombreCategoria() : "Sin categoría";
                BigDecimal monto = gasto.getMonto() != null ? gasto.getMonto() : BigDecimal.ZERO;
                gastosPorCategoria.merge(categoria, monto, BigDecimal::add);
            }
            
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            for (Map.Entry<String, BigDecimal> entry : gastosPorCategoria.entrySet()) {
                pieData.add(new PieChart.Data(entry.getKey(), entry.getValue().doubleValue()));
            }
            chartGastos.setData(pieData);
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Gastos");
            
            String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun"};
            Random random = new Random();
            for (String mes : meses) {
                series.getData().add(new XYChart.Data<>(mes, random.nextDouble() * 1000));
            }
            
            chartMensual.getData().clear();
            chartMensual.getData().add(series);
            
            listaGastos.getItems().clear();
            for (GastoDTO gasto : gastos.stream().limit(5).collect(Collectors.toList())) {
                String item = String.format("%s - $%.2f - %s", 
                    gasto.getDescripcion() != null ? gasto.getDescripcion() : "Sin descripción",
                    gasto.getMonto() != null ? gasto.getMonto() : BigDecimal.ZERO,
                    gasto.getNombreCategoria());
                listaGastos.getItems().add(item);
            }
            
            listaCuentas.getItems().clear();
            for (CuentaFinancieraDTO cuenta : cuentas) {
                BigDecimal saldo = cuenta.getSaldoActual() != null ? cuenta.getSaldoActual() : BigDecimal.ZERO;
                String item = String.format("%s - Saldo: $%.2f", cuenta.getNombre(), saldo);
                listaCuentas.getItems().add(item);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error al cargar datos: " + e.getMessage());
        }
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    @Override
    public void stop() throws Exception {
        if (em != null && em.isOpen()) {
            em.close();
        }
        super.stop();
    }
}
