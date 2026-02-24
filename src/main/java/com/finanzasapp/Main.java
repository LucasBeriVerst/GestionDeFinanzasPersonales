package com.finanzasapp;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.controller.CategoriaGastoController;
import com.finanzasapp.backend.controller.CuentaFinancieraController;
import com.finanzasapp.backend.controller.GastoController;
import com.finanzasapp.backend.controller.LoginController;
import com.finanzasapp.backend.repository.*;
import com.finanzasapp.backend.repository.impl.*;
import com.finanzasapp.backend.service.impl.*;
import com.finanzasapp.backend.service.interfaces.*;
import com.finanzasapp.frontend.swing.LoginView;
import com.finanzasapp.frontend.swing.MainView;
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
