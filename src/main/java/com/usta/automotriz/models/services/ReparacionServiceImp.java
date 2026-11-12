package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ReparacionEntity;
import com.usta.automotriz.enums.EstadoReparacion;
import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.models.daos.ReparacionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReparacionServiceImp implements ReparacionService {

    @Autowired
    private ReparacionDao reparacionDao;

    @Override
    public List<ReparacionEntity> listarTodas() {
        return reparacionDao.findAll();
    }

    @Override
    public void guardarReparacion(ReparacionEntity reparacion) throws Exception {
        if (reparacion.getIdUsuario() == null || reparacion.getIdUsuario().getRol() != Rol.Mecanico) {
            throw new Exception("Error: El responsable de la reparación debe ser un MECÁNICO.");
        }

        if (reparacion.getIdVehiculo() == null || !reparacion.getIdVehiculo().getActivoVehiculo()) {
            throw new Exception("Error: No se puede registrar una reparación a un vehículo inactivo.");
        }

        if (reparacion.getIdReparacion() == null) {
            reparacion.setEstadoReparacion(EstadoReparacion.EN_PROCESO);

            if (reparacion.getFechaReparacion() == null) {
                reparacion.setFechaReparacion(LocalDateTime.now());
            }
        }

        reparacionDao.save(reparacion);
    }

    @Override
    public Optional<ReparacionEntity> buscarPorId(Long id) {
        return reparacionDao.findById(id);
    }

    @Override
    public void cancelarReparacion(Long id) throws Exception {
        cambiarEstado(id, "CANCELADO");
    }

    @Override
    public void cambiarEstado(Long id, String nuevoEstadoStr) throws Exception {
        Optional<ReparacionEntity> reparacionOpt = reparacionDao.findById(id);

        if (reparacionOpt.isPresent()) {
            ReparacionEntity reparacion = reparacionOpt.get();
            EstadoReparacion estadoActual = reparacion.getEstadoReparacion();
            EstadoReparacion nuevoEstado = EstadoReparacion.valueOf(nuevoEstadoStr);

            if ((estadoActual == EstadoReparacion.TERMINADO || estadoActual == EstadoReparacion.CANCELADO)
                    && nuevoEstado == EstadoReparacion.EN_PROCESO) {
                throw new Exception("Error: Una reparación terminada o cancelada no puede volver a estar en proceso.");
            }

            reparacion.setEstadoReparacion(nuevoEstado);
            reparacionDao.save(reparacion);
        } else {
            throw new Exception("Error: La reparación no existe.");
        }
    }
}