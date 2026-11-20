package com.usta.automotriz.config;

import com.usta.automotriz.entities.UsuarioEntity;
import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.models.daos.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioDao.existsByUserNameUsuario("admin")) {
            UsuarioEntity admin = new UsuarioEntity();
            admin.setNombreUsuario("Administrador General");
            admin.setUserNameUsuario("admin");
            admin.setCorreoUsuario("admin@taller.com");
            admin.setPasswordUsuario(passwordEncoder.encode("admin123"));
            admin.setEsActivo(true);
            admin.setRol(Rol.Administrador);
            usuarioDao.save(admin);
            System.out.println("Usuario administrador precargado -> user: admin | pass: admin123");
        }
    }
}