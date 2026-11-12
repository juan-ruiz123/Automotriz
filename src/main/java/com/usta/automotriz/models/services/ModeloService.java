package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ModeloEntity;

import java.util.List;
import java.util.Optional;

public interface ModeloService {
    List<ModeloEntity> listarTodos();
    void guardarModelo(ModeloEntity modelo) throws Exception;
    Optional<ModeloEntity> buscarPorId(Long id);
    void darDeBaja(Long id) throws Exception;
}
