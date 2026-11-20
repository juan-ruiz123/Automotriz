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
            vehiculo.setPlacaVehiculo(vehiculo.getPlacaVehiculo().trim().toUpperCase());
        }

        // RN-01: Formato ABC-123
        String regexPlaca = "^[A-Z]{3}-[0-9]{3}$";
        if (vehiculo.getPlacaVehiculo() == null || !vehiculo.getPlacaVehiculo().matches(regexPlaca)) {
            throw new Exception("El formato de la placa es inválido. Debe ser ABC-123 (ej. ABC-123).");
        }

        // RN-02 y Modelo obligatorio
        if (vehiculo.getTipoPlaca() == null) {
            throw new Exception("Debe seleccionar el tipo de placa (AMARILLA o BLANCA).");
        }
        if (vehiculo.getIdModelo() == null || vehiculo.getIdModelo().getIdModelo() == null) {
            throw new Exception("Debe seleccionar un modelo para el vehículo.");
        }

        if (vehiculo.getIdVehiculo() == null) {
            if (vehiculoDao.existsByPlacaVehiculo(vehiculo.getPlacaVehiculo())) {
                throw new Exception("La placa " + vehiculo.getPlacaVehiculo() + " ya está registrada en el sistema.");
            }
            vehiculo.setActivoVehiculo(true);
        } else {
            VehiculoEntity existente = vehiculoDao.findById(vehiculo.getIdVehiculo())
                    .orElseThrow(() -> new Exception("El vehículo no existe."));

            if (!existente.getPlacaVehiculo().equals(vehiculo.getPlacaVehiculo())
                    && vehiculoDao.existsByPlacaVehiculo(vehiculo.getPlacaVehiculo())) {
                throw new Exception("La placa " + vehiculo.getPlacaVehiculo() + " ya pertenece a otro vehículo.");
            }
            vehiculo.setActivoVehiculo(existente.getActivoVehiculo());
        }

        vehiculoDao.save(vehiculo);
    }

    @Override
    public Optional<VehiculoEntity> buscarPorId(Long id) {
        return vehiculoDao.findById(id);
    }

    @Override
    public Optional<VehiculoEntity> buscarPorPlaca(String placa) {
        return vehiculoDao.findByPlacaVehiculo(placa.trim().toUpperCase());
    }

    @Override
    public void darDeBaja(Long id) throws Exception {
        VehiculoEntity vehiculo = vehiculoDao.findById(id)
                .orElseThrow(() -> new Exception("El vehículo no existe."));
        vehiculo.setActivoVehiculo(false);
        vehiculoDao.save(vehiculo);
    }
}