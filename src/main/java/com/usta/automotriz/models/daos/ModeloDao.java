package com.usta.automotriz.models.daos;

import com.usta.automotriz.entities.ModeloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeloDao extends JpaRepository<ModeloEntity, Long> {
    boolean existsByNombreModeloAndMarcaModelo(String nombreModelo, String marcaModelo);
}
