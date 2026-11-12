package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.VehiculoEntity;
import com.usta.automotriz.models.daos.VehiculoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoServiceImp implements VehiculoService {
    @Autowired
    private VehiculoDao vehiculoDao;

    @Override
    public List<VehiculoEntity> listarTodos() {
        return vehiculoDao.findAll();
    }

    @Override
    public void guardarVehiculo(VehiculoEntity vehiculo) throws Exception {
        if (vehiculo.getPlacaVehiculo() != null) {
            vehiculo.setPlacaVehiculo(vehiculo.getPlacaVehiculo().toUpperCase());
        }

        String regexPlaca = "^[A-Z]{3}-[0-9]{3}$";
        if (!vehiculo.getPlacaVehiculo().matches(regexPlaca)) {
            throw new Exception("Error: El formato de la placa es inválido. Debe ser ABC-123.");
        }

        if (vehiculo.getIdVehiculo() == null && vehiculoDao.existsByPlacaVehiculo(vehiculo.getPlacaVehiculo())) {
            throw new Exception("Error: La placa " + vehiculo.getPlacaVehiculo() + " ya está registrada en el sistema.");
        }

        if (vehiculo.getIdVehiculo() == null) {
            vehiculo.setActivoVehiculo(true);
        }

        vehiculoDao.save(vehiculo);
    }

    @Override
    public Optional<VehiculoEntity> buscarPorId(Long id) {
        return vehiculoDao.findById(id);
    }

    @Override
    public Optional<VehiculoEntity> buscarPorPlaca(String placa) {
        return vehiculoDao.findByPlacaVehiculo(placa.toUpperCase());
    }

    @Override
    public void darDeBaja(Long id) throws Exception {
        Optional<VehiculoEntity> vehiculoExistente = vehiculoDao.findById(id);
        if (vehiculoExistente.isPresent()) {
            VehiculoEntity vehiculoUpdate = vehiculoExistente.get();
            vehiculoUpdate.setActivoVehiculo(false);
            vehiculoDao.save(vehiculoUpdate);
        } else {
            throw new Exception("Error: El vehículo no existe.");
        }
    }
}
