package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.ModeloEntity;
import com.usta.automotriz.models.services.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("modelo", new ModeloEntity());
        return "modelo/crear";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return modeloService.buscarPorId(id).map(modelo -> {
            model.addAttribute("modelo", modelo);
            return "modelo/crear";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "El modelo solicitado no existe.");
            return "redirect:/modelos/listar";
        });
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return modeloService.buscarPorId(id).map(modelo -> {
            model.addAttribute("modelo", modelo);
            return "modelo/detalle";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "El modelo solicitado no existe.");
            return "redirect:/modelos/listar";
        });
    }

    @PostMapping("/guardar")
    public String guardarModelo(@ModelAttribute ModeloEntity modelo, Model modelView, RedirectAttributes flash) {
        try {
            boolean esNuevo = (modelo.getIdModelo() == null);
            modeloService.guardarModelo(modelo);
            flash.addFlashAttribute("exito", esNuevo ? "Modelo registrado correctamente." : "Modelo actualizado correctamente.");
            return "redirect:/modelos/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("modelo", modelo);
            return "modelo/crear";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaModelo(@PathVariable Long id, RedirectAttributes flash) {
        try {
            modeloService.darDeBaja(id);
            flash.addFlashAttribute("exito", "Modelo dado de baja exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/modelos/listar";
    }
}