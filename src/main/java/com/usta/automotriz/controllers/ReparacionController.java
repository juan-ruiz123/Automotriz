package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.ReparacionEntity;
import com.usta.automotriz.models.services.ReparacionService;
import com.usta.automotriz.models.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reparaciones")
public class ReparacionController {

    @Autowired
    private ReparacionService reparacionService;

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping("/listar")
    public String listarReparaciones(Model model) {
        model.addAttribute("reparaciones", reparacionService.listarTodas());
        return "reparacion/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("reparacion", new ReparacionEntity());
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        return "reparacion/crear";
    }

    @PostMapping("/guardar")
    public String guardarReparacion(@ModelAttribute ReparacionEntity reparacion, Model modelView) {
        try {
            reparacionService.guardarReparacion(reparacion);
            return "redirect:/reparaciones/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("reparacion", reparacion);
            modelView.addAttribute("vehiculos", vehiculoService.listarTodos());
            return "reparacion/crear";
        }
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarReparacion(@PathVariable Long id, Model modelView) {
        try {
            reparacionService.cancelarReparacion(id);
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
        }
        return "redirect:/reparaciones/listar";
    }
}