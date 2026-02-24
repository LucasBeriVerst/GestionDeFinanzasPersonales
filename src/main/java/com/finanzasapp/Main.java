package com.finanzasapp;

import com.finanzasapp.config.JPAConfig;
import com.finanzasapp.controller.CategoriaGastoController;
import com.finanzasapp.controller.CuentaFinancieraController;
import com.finanzasapp.controller.GastoController;
import com.finanzasapp.controller.LoginController;
import com.finanzasapp.repository.*;
import com.finanzasapp.repository.impl.*;
import com.finanzasapp.service.impl.*;
import com.finanzasapp.service.interfaces.*;
import com.finanzasapp.view.swing.LoginView;
import com.finanzasapp.view.swing.MainView;
import jakarta.persistence.EntityManager;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            EntityManager em = JPAConfig.getEntityManager();

            UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl(em);
            CuentaFinancieraRepository cuentaRepository = new CuentaFinancieraRepositoryImpl(em);
            CategoriaGastoRepository categoriaRepository = new CategoriaGastoRepositoryImpl(em);
            GastoRepository gastoRepository = new GastoRepositoryImpl(em);
            MovimientoRepository movimientoRepository = new MovimientoRepositoryImpl(em);
            TipoCuentaRepository tipoCuentaRepository = new TipoCuentaRepositoryImpl(em);
            MonedaRepository monedaRepository = new MonedaRepositoryImpl(em);

            IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepository);
            ICuentaFinancieraService cuentaService = new CuentaFinancieraServiceImpl(
                cuentaRepository, usuarioRepository, tipoCuentaRepository, monedaRepository);
            ICategoriaGastoService categoriaService = new CategoriaGastoServiceImpl(categoriaRepository);
            IGastoService gastoService = new GastoServiceImpl(
                em, gastoRepository, cuentaRepository, usuarioRepository, 
                categoriaRepository, monedaRepository, movimientoRepository);
            IAutenticacionService autenticacionService = new AutenticacionServiceImpl(usuarioRepository);

            LoginController loginController = new LoginController(autenticacionService, usuarioService);
            CuentaFinancieraController cuentaController = new CuentaFinancieraController(cuentaService);
            CategoriaGastoController categoriaController = new CategoriaGastoController(categoriaService);
            GastoController gastoController = new GastoController(gastoService);

            MainView mainView = new MainView();
            mainView.setControllers(loginController, cuentaController, categoriaController, gastoController);

            LoginView loginView = new LoginView();
            loginView.setLoginController(loginController);
            loginView.setMainView(mainView);

            loginView.setVisible(true);
        });
    }
}
