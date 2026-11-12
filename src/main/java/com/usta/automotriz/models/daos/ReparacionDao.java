package com.usta.automotriz.models.daos;

import com.usta.automotriz.entities.ReparacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReparacionDao extends JpaRepository<ReparacionEntity, Long> {
}