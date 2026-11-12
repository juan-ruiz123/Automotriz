package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.VehiculoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class VehiculoServiceTest {

    @Autowired
    private VehiculoService vehiculoService;

    @Test
    void testValidarFormatoPlaca_RN01() {
        System.out.println("Iniciando prueba de backend: Validación de Placa (RN-01)...");

        VehiculoEntity vehiculoPrueba = new VehiculoEntity();
        vehiculoPrueba.setPlacaVehiculo("AB-1234");

        Exception exception = assertThrows(Exception.class, () -> {
            vehiculoService.guardarVehiculo(vehiculoPrueba);
        });

        assertTrue(exception.getMessage().contains("ABC-123"));

        System.out.println("✅ PRUEBA EXITOSA: El backend bloqueó la placa inválida correctamente.");
    }
}
