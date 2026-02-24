package com.finanzasapp.backend.controller;

import com.finanzasapp.backend.model.dto.CategoriaGastoCreateDTO;
import com.finanzasapp.backend.model.dto.CategoriaGastoDTO;
import com.finanzasapp.backend.model.enums.TipoCategoria;
import com.finanzasapp.backend.service.interfaces.ICategoriaGastoService;
import java.util.List;

public class CategoriaGastoController {

    private final ICategoriaGastoService categoriaService;

    public CategoriaGastoController(ICategoriaGastoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public CategoriaGastoDTO crear(String nombre, String descripcion, String icono, TipoCategoria tipo) {
        try {
            CategoriaGastoCreateDTO dto = new CategoriaGastoCreateDTO();
            dto.setNombre(nombre);
            dto.setDescripcion(descripcion);
            dto.setIcono(icono);
            dto.setTipo(tipo);
            return categoriaService.crear(dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CategoriaGastoDTO actualizar(Long id, String nombre, String descripcion, String icono, TipoCategoria tipo) {
        try {
            CategoriaGastoCreateDTO dto = new CategoriaGastoCreateDTO();
            dto.setNombre(nombre);
            dto.setDescripcion(descripcion);
            dto.setIcono(icono);
            dto.setTipo(tipo);
            return categoriaService.actualizar(id, dto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CategoriaGastoDTO buscarPorId(Long id) {
        return categoriaService.buscarPorId(id);
    }

    public List<CategoriaGastoDTO> listarTodos() {
        return categoriaService.listarTodos();
    }

    public List<CategoriaGastoDTO> listarActivos() {
        return categoriaService.listarActivos();
    }

    public void eliminar(Long id) {
        categoriaService.eliminar(id);
    }
}
