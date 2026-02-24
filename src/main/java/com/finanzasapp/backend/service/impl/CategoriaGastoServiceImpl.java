package com.finanzasapp.service.impl;

import com.finanzasapp.model.dto.CategoriaGastoCreateDTO;
import com.finanzasapp.model.dto.CategoriaGastoDTO;
import com.finanzasapp.model.entity.CategoriaGasto;
import com.finanzasapp.repository.CategoriaGastoRepository;
import com.finanzasapp.service.interfaces.ICategoriaGastoService;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriaGastoServiceImpl implements ICategoriaGastoService {

    private final CategoriaGastoRepository categoriaRepository;

    public CategoriaGastoServiceImpl(CategoriaGastoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaGastoDTO crear(CategoriaGastoCreateDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (categoriaRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        CategoriaGasto categoria = new CategoriaGasto();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setIcono(dto.getIcono());
        categoria.setTipo(dto.getTipo());

        categoria = categoriaRepository.save(categoria);
        return toDTO(categoria);
    }

    @Override
    public CategoriaGastoDTO actualizar(Long id, CategoriaGastoCreateDTO dto) {
        CategoriaGasto categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        if (dto.getNombre() != null && !dto.getNombre().isEmpty()) {
            categoria.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            categoria.setDescripcion(dto.getDescripcion());
        }
        if (dto.getIcono() != null) {
            categoria.setIcono(dto.getIcono());
        }
        if (dto.getTipo() != null) {
            categoria.setTipo(dto.getTipo());
        }

        categoria = categoriaRepository.save(categoria);
        return toDTO(categoria);
    }

    @Override
    public CategoriaGastoDTO buscarPorId(Long id) {
        CategoriaGasto categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        return toDTO(categoria);
    }

    @Override
    public List<CategoriaGastoDTO> listarTodos() {
        return categoriaRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaGastoDTO> listarActivos() {
        return categoriaRepository.findByActivoTrue().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        CategoriaGasto categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        categoria.softDelete();
        categoriaRepository.save(categoria);
    }

    private CategoriaGastoDTO toDTO(CategoriaGasto categoria) {
        return new CategoriaGastoDTO(
            categoria.getId(),
            categoria.getNombre(),
            categoria.getDescripcion(),
            categoria.getIcono(),
            categoria.getTipo(),
            categoria.getActivo()
        );
    }
}
