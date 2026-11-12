package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.VehiculoEntity;

import java.util.List;
import java.util.Optional;

public interface VehiculoService {
    List<VehiculoEntity> listarTodos();
    void guardarVehiculo(VehiculoEntity vehiculo) throws Exception;
    Optional<VehiculoEntity> buscarPorId(Long id);
    Optional<VehiculoEntity> buscarPorPlaca(String placa);
    void darDeBaja(Long id) throws Exception;
}
