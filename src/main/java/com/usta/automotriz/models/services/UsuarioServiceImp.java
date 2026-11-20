package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.UsuarioEntity;
import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.models.daos.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioEntity> listarTodos() {
        return usuarioDao.findAll();
    }

    @Override
    public List<UsuarioEntity> listarMecanicos() {
        return usuarioDao.findByRol(Rol.Mecanico).stream()
                .filter(UsuarioEntity::getEsActivo)
                .toList();
    }

    @Override
    public void guardarUsuario(UsuarioEntity usuario) throws Exception {
        if (usuario.getIdUsuario() == null) {
            if (usuarioDao.existsByUserNameUsuario(usuario.getUserNameUsuario())) {
                throw new Exception("El nombre de usuario '" + usuario.getUserNameUsuario() + "' ya está en uso.");
            }
            if (usuario.getPasswordUsuario() == null || usuario.getPasswordUsuario().isBlank()) {
                throw new Exception("La contraseña es obligatoria para un usuario nuevo.");
            }
            usuario.setPasswordUsuario(passwordEncoder.encode(usuario.getPasswordUsuario()));
            usuario.setEsActivo(true);
        } else {
            UsuarioEntity existente = usuarioDao.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new Exception("El usuario no existe."));

            if (!existente.getUserNameUsuario().equals(usuario.getUserNameUsuario())
                    && usuarioDao.existsByUserNameUsuario(usuario.getUserNameUsuario())) {
                throw new Exception("El nombre de usuario '" + usuario.getUserNameUsuario() + "' ya está en uso.");
            }

            if (usuario.getPasswordUsuario() == null || usuario.getPasswordUsuario().isBlank()) {
                usuario.setPasswordUsuario(existente.getPasswordUsuario());
            } else {
                usuario.setPasswordUsuario(passwordEncoder.encode(usuario.getPasswordUsuario()));
            }

            if (usuario.getEsActivo() == null) {
                usuario.setEsActivo(existente.getEsActivo());
            }
        }

        usuarioDao.save(usuario);
    }

    @Override
    public Optional<UsuarioEntity> buscarPorId(Long id) {
        return usuarioDao.findById(id);
    }

    @Override
    public Optional<UsuarioEntity> buscarPorUsername(String username) {
        return usuarioDao.findByUserNameUsuario(username);
    }

    @Override
    public void darDeBaja(Long id) throws Exception {
        UsuarioEntity usuario = usuarioDao.findById(id)
                .orElseThrow(() -> new Exception("El usuario no existe."));
        usuario.setEsActivo(false);
        usuarioDao.save(usuario);
    }
}