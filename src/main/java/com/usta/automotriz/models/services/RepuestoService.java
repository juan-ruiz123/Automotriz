package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.RepuestoEntity;

import java.util.List;
import java.util.Optional;

public interface RepuestoService {
    List<RepuestoEntity> listarTodos();
    void guardarRepuesto(RepuestoEntity repuesto) throws Exception;
    Optional<RepuestoEntity> buscarPorId(Long id);
    void darDeBaja(Long id) throws Exception;

    List<RepuestoEntity> buscarPorModelo(Long idModelo);
}
