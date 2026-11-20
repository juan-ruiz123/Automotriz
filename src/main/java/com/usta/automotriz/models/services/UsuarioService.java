package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.UsuarioEntity;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioEntity> listarTodos();
    List<UsuarioEntity> listarMecanicos();
    void guardarUsuario(UsuarioEntity usuario) throws Exception;
    Optional<UsuarioEntity> buscarPorId(Long id);
    Optional<UsuarioEntity> buscarPorUsername(String username);
    void darDeBaja(Long id) throws Exception;
}