package com.usta.automotriz.controllers;

import com.usta.automotriz.models.services.ModeloService;
import com.usta.automotriz.entities.ModeloEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/modelos")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @GetMapping("/listar")
    public String listarModelos(Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        return "modelo/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("modelo", new ModeloEntity());
        return "modelo/crear";
    }

    @PostMapping("/guardar")
    public String guardarModelo(@ModelAttribute ModeloEntity modelo, Model modelView) {
        try {
            modeloService.guardarModelo(modelo);
            return "redirect:/modelos/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("modelo", modelo);
            return "modelo/crear";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaModelo(@PathVariable Long id, Model modelView) {
        try {
            modeloService.darDeBaja(id);
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
        }
        return "redirect:/modelo/listar";
    }
}