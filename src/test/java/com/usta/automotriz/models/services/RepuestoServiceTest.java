package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.RepuestoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RepuestoServiceTest {

    @Autowired
    private RepuestoService repuestoService;

    @Test
    void testValidarEan_RN05() {
        RepuestoEntity repuesto = new RepuestoEntity();
        repuesto.setNombreRepuesto("Filtro");
        repuesto.setPrecioRepuesto(15000.0);
        repuesto.setStockRepuesto(10);
        repuesto.setEanRepuesto("123");

        Exception exception = assertThrows(Exception.class, () -> {
            repuestoService.guardarRepuesto(repuesto);
        });

        assertTrue(exception.getMessage().contains("13 dígitos numéricos"));
    }

    @Test
    void testValidarPrecio_RN06() {
        RepuestoEntity repuesto = new RepuestoEntity();
        repuesto.setNombreRepuesto("Filtro");
        repuesto.setPrecioRepuesto(-500.0);
        repuesto.setStockRepuesto(10);
        repuesto.setEanRepuesto("1234567890123");

        Exception exception = assertThrows(Exception.class, () -> {
            repuestoService.guardarRepuesto(repuesto);
        });

        assertTrue(exception.getMessage().contains("mayor a 0"));
    }
}