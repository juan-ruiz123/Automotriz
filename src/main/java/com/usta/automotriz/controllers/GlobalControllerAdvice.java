package com.usta.automotriz.controllers;

import com.usta.automotriz.models.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void agregarDatosUsuarioGlobal(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            boolean esAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMINISTRADOR"));
            model.addAttribute("esAdmin", esAdmin);
            model.addAttribute("usernameActual", auth.getName());
            usuarioService.buscarPorUsername(auth.getName()).ifPresent(u -> {
                model.addAttribute("nombreUsuarioActual", u.getNombreUsuario());
                model.addAttribute("rolUsuarioActual", u.getRol().name());
            });
        } else {
            model.addAttribute("esAdmin", false);
        }
    }
}