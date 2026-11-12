package com.usta.automotriz.models.daos;

import com.usta.automotriz.entities.RepuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepuestoDao extends JpaRepository<RepuestoEntity, Long> {
    boolean existsByEanRepuesto(String eanRepuesto);

    Optional<RepuestoEntity> findByEanRepuesto(String eanRepuesto);
}
