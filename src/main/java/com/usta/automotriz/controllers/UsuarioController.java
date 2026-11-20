package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.UsuarioEntity;
import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.models.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuario/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new UsuarioEntity());
        model.addAttribute("roles", Rol.values());
        return "usuario/crear";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        UsuarioEntity usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios/listar";
        }
        usuario.setPasswordUsuario(""); // Se envía vacío para no mostrar el hash BCrypt
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "usuario/crear";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute UsuarioEntity usuario, Model modelView) {
        try {
            usuarioService.guardarUsuario(usuario);
            return "redirect:/usuarios/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("usuario", usuario);
            modelView.addAttribute("roles", Rol.values());
            return "usuario/crear";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaUsuario(@PathVariable Long id, Model modelView) {
        try {
            usuarioService.darDeBaja(id);
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }
}