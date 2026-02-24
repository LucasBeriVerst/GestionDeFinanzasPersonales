package com.finanzasapp.backend.service.impl;

import com.finanzasapp.backend.exception.ValidationException;
import com.finanzasapp.backend.model.dto.CategoriaGastoCreateDTO;
import com.finanzasapp.backend.model.dto.CategoriaGastoDTO;
import com.finanzasapp.backend.model.entity.CategoriaGasto;
import com.finanzasapp.backend.repository.CategoriaGastoRepository;
import com.finanzasapp.backend.service.interfaces.ICategoriaGastoService;
import com.finanzasapp.backend.validator.CategoriaGastoValidator;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriaGastoServiceImpl implements ICategoriaGastoService {

    private final CategoriaGastoRepository categoriaRepository;

    public CategoriaGastoServiceImpl(CategoriaGastoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public CategoriaGastoDTO crear(CategoriaGastoCreateDTO dto) {
        List<String> errores = CategoriaGastoValidator.validarCreacion(dto);
        
        if (categoriaRepository.findByNombre(dto.getNombre()).isPresent()) {
            errores.add("Ya existe una categoría con ese nombre");
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
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
