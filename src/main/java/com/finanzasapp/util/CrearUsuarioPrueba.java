package com.finanzasapp.util;

import com.finanzasapp.database.JPAConfig;
import com.finanzasapp.backend.model.dto.UsuarioCreateDTO;
import com.finanzasapp.backend.model.dto.UsuarioDTO;
import com.finanzasapp.backend.repository.*;
import com.finanzasapp.backend.repository.impl.*;
import com.finanzasapp.backend.service.impl.*;
import com.finanzasapp.backend.service.interfaces.*;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrearUsuarioPrueba {

    private static final Logger LOG = LoggerFactory.getLogger(CrearUsuarioPrueba.class);
    
    public static void main(String[] args) {
        EntityManager em = JPAConfig.getEntityManager();
        
        try {
            UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl(em);
            IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepo);
            
            // Buscar usuario existente o crear uno nuevo
            String username = "admin";
            var usuarioExistente = usuarioRepo.findByUsername(username);
            
            if (usuarioExistente.isPresent()) {
                LOG.info("Usuario existente: {}", username);
                LOG.info("Password: admin123");
            } else {
                UsuarioCreateDTO nuevo = new UsuarioCreateDTO();
                nuevo.setUsername(username);
                nuevo.setPassword("admin123");
                nuevo.setEmail("admin@finanzasapp.com");
                
                UsuarioDTO creado = usuarioService.crear(nuevo);
                LOG.info("Usuario creado: {}", username);
                LOG.info("Password: admin123");
            }
            
            LOG.info("\n========================================");
            LOG.info("CREDENCIALES PARA LOGIN:");
            LOG.info("========================================");
            LOG.info("Usuario: {}", username);
            LOG.info("Password: admin123");
            LOG.info("========================================");
            
        } catch (Exception e) {
            LOG.error("Error: {}", e.getMessage());
        } finally {
            em.close();
            JPAConfig.closeEntityManagerFactory();
        }
    }
}
