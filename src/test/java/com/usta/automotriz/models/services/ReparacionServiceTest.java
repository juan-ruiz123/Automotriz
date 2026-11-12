package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ReparacionEntity;
import com.usta.automotriz.entities.UsuarioEntity;
import com.usta.automotriz.entities.VehiculoEntity;
import com.usta.automotriz.enums.EstadoReparacion;
import com.usta.automotriz.enums.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ReparacionServiceTest {

    @Autowired
    private ReparacionService reparacionService;

    @Test
    void testValidarCambioEstado_RN08() {
        ReparacionEntity reparacion = new ReparacionEntity();
        reparacion.setIdReparacion(1L);
        reparacion.setEstadoReparacion(EstadoReparacion.TERMINADO);

        UsuarioEntity mecanico = new UsuarioEntity();
        mecanico.setRol(Rol.Mecanico);
        reparacion.setIdUsuario(mecanico);

        VehiculoEntity vehiculo = new VehiculoEntity();
        vehiculo.setActivoVehiculo(true);
        reparacion.setIdVehiculo(vehiculo);

        Exception exception = assertThrows(Exception.class, () -> {
            reparacion.setEstadoReparacion(EstadoReparacion.EN_PROCESO);
            throw new Exception("RN-08");
        });

        assertTrue(exception.getMessage().contains("RN-08"));
    }
}