package com.usta.automotriz.models.daos;

import com.usta.automotriz.entities.VehiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehiculoDao extends JpaRepository<VehiculoEntity, Long> {
    boolean existsByPlacaVehiculo(String placaVehiculo);

    Optional<VehiculoEntity> findByPlacaVehiculo(String placaVehiculo);
}
