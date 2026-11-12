package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ReparacionEntity;
import java.util.List;
import java.util.Optional;

public interface ReparacionService {
    List<ReparacionEntity> listarTodas();
    void guardarReparacion(ReparacionEntity reparacion) throws Exception;
    Optional<ReparacionEntity> buscarPorId(Long id);
    void cancelarReparacion(Long id) throws Exception;
    void cambiarEstado(Long id, String nuevoEstado) throws Exception;
}