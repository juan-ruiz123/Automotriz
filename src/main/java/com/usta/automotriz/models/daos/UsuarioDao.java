package com.usta.automotriz.models.daos;

import com.usta.automotriz.entities.UsuarioEntity;
import com.usta.automotriz.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioDao extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUserNameUsuario(String userNameUsuario);
    boolean existsByUserNameUsuario(String userNameUsuario);
    List<UsuarioEntity> findByRol(Rol rol);
}