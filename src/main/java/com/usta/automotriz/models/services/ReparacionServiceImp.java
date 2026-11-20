package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ReparacionEntity;
import com.usta.automotriz.entities.UsuarioEntity;
import com.usta.automotriz.entities.VehiculoEntity;
import com.usta.automotriz.enums.EstadoReparacion;
import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.models.daos.ReparacionDao;
import com.usta.automotriz.models.daos.UsuarioDao;
import com.usta.automotriz.models.daos.VehiculoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReparacionServiceImp implements ReparacionService {

    @Autowired
    private ReparacionDao reparacionDao;

    @Autowired
    private VehiculoDao vehiculoDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private EmailService emailService;

    @Override
    public List<ReparacionEntity> listarTodas() {
        return reparacionDao.findAll();
    }

    @Override
    public List<ReparacionEntity> listarPorMecanicoUsername(String username) {
        return reparacionDao.findAll().stream()
                .filter(r -> r.getIdUsuario() != null && r.getIdUsuario().getUserNameUsuario().equals(username))
                .toList();
    }

    @Override
    @Transactional
    public void guardarReparacion(ReparacionEntity reparacion) throws Exception {
        if (reparacion.getTipoReparacion() == null) {
            throw new Exception("Debe seleccionar el tipo de reparación.");
        }

        if (reparacion.getIdUsuario() == null || reparacion.getIdUsuario().getIdUsuario() == null) {
            throw new Exception("Debe asignar un mecánico responsable.");
        }
        UsuarioEntity mecanico = usuarioDao.findById(reparacion.getIdUsuario().getIdUsuario())
                .orElseThrow(() -> new Exception("El mecánico seleccionado no existe."));

        // RN-10: Rol MECANICO obligatorio
        if (mecanico.getRol() != Rol.Mecanico || !mecanico.getEsActivo()) {
            throw new Exception("El responsable de la reparación debe ser un MECÁNICO activo.");
        }

        if (reparacion.getIdVehiculo() == null || reparacion.getIdVehiculo().getIdVehiculo() == null) {
            throw new Exception("Debe seleccionar un vehículo.");
        }
        VehiculoEntity vehiculo = vehiculoDao.findById(reparacion.getIdVehiculo().getIdVehiculo())
                .orElseThrow(() -> new Exception("El vehículo seleccionado no existe."));

        if (!vehiculo.getActivoVehiculo()) {
            throw new Exception("No se puede registrar una reparación a un vehículo dado de baja.");
        }

        reparacion.setIdUsuario(mecanico);
        reparacion.setIdVehiculo(vehiculo);

        if (reparacion.getIdReparacion() == null) {
            // RN-08: Toda reparación nace EN_PROCESO
            reparacion.setEstadoReparacion(EstadoReparacion.EN_PROCESO);
            if (reparacion.getFechaReparacion() == null) {
                reparacion.setFechaReparacion(LocalDateTime.now());
            }
        } else {
            ReparacionEntity existente = reparacionDao.findById(reparacion.getIdReparacion())
                    .orElseThrow(() -> new Exception("La reparación no existe."));
            reparacion.setFechaReparacion(existente.getFechaReparacion());
            if (reparacion.getEstadoReparacion() == null) {
                reparacion.setEstadoReparacion(existente.getEstadoReparacion());
            }
        }

        reparacionDao.save(reparacion);
    }

    @Override
    public Optional<ReparacionEntity> buscarPorId(Long id) {
        return reparacionDao.findById(id);
    }

    @Override
    @Transactional
    public void cancelarReparacion(Long id) throws Exception {
        cambiarEstado(id, "CANCELADO");
    }

    @Override
    @Transactional
    public void cambiarEstado(Long id, String nuevoEstadoStr) throws Exception {
        ReparacionEntity reparacion = reparacionDao.findById(id)
                .orElseThrow(() -> new Exception("La reparación no existe."));

        EstadoReparacion estadoActual = reparacion.getEstadoReparacion();
        EstadoReparacion nuevoEstado = EstadoReparacion.valueOf(nuevoEstadoStr);

        // RN-08: Estados finales no vuelven a EN_PROCESO
        if ((estadoActual == EstadoReparacion.TERMINADO || estadoActual == EstadoReparacion.CANCELADO)
                && nuevoEstado == EstadoReparacion.EN_PROCESO) {
            throw new Exception("Una reparación terminada o cancelada no puede volver a estar en proceso.");
        }

        reparacion.setEstadoReparacion(nuevoEstado);
        reparacionDao.save(reparacion);

        // Notificación por correo al dueño cuando pasa a TERMINADO
        if (nuevoEstado == EstadoReparacion.TERMINADO && estadoActual != EstadoReparacion.TERMINADO) {
            emailService.enviarNotificacionReparacionTerminada(reparacion);
        }
    }
}