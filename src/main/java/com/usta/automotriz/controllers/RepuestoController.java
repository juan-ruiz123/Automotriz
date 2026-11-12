package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.RepuestoEntity;
import com.usta.automotriz.models.services.ModeloService;
import com.usta.automotriz.models.services.RepuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/repuestos")
public class RepuestoController {

    @Autowired
    private RepuestoService repuestoService;

    @Autowired
    private ModeloService modeloService;

    @GetMapping("/listar")
    public String listarRepuestos(Model model) {
        model.addAttribute("repuestos", repuestoService.listarTodos());
        return "repuesto/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("repuesto", new RepuestoEntity());
        model.addAttribute("listaModelos", modeloService.listarTodos());
        return "repuesto/crear";
    }

    @PostMapping("/guardar")
    public String guardarRepuesto(@ModelAttribute RepuestoEntity repuesto, Model modelView) {
        try {
            repuestoService.guardarRepuesto(repuesto);
            return "redirect:/repuestos/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("repuesto", repuesto);
            modelView.addAttribute("listaModelos", modeloService.listarTodos());
            return "repuesto/crear";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaRepuesto(@PathVariable Long id, Model modelView) {
        try {
            repuestoService.darDeBaja(id);
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
        }
        return "redirect:/repuestos/listar";
    }
}