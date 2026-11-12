package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ModeloEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ModeloServiceTest {

    @Autowired
    private ModeloService modeloService;

    @Test
    void testValidarCombinacionUnica_RN04() throws Exception {
        ModeloEntity modelo1 = new ModeloEntity();
        modelo1.setNombreModelo("Corolla");
        modelo1.setMarcaModelo("Toyota");
        modelo1.setActivoModelo(true);
        modeloService.guardarModelo(modelo1);

        ModeloEntity modelo2 = new ModeloEntity();
        modelo2.setNombreModelo("Corolla");
        modelo2.setMarcaModelo("Toyota");
        modelo2.setActivoModelo(true);

        Exception exception = assertThrows(Exception.class, () -> {
            modeloService.guardarModelo(modelo2);
        });

        String mensajeError = exception.getMessage();
        assertTrue(mensajeError.contains("Ya existe un modelo registrado") &&
                mensajeError.contains("Corolla") &&
                mensajeError.contains("Toyota"));
    }
}