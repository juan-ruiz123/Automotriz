package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.VehiculoEntity;
import com.usta.automotriz.models.services.ModeloService;
import com.usta.automotriz.models.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("/vehiculos")
public class VehiculoController {
    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ModeloService modeloService;

    @GetMapping("/listar")
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        return "vehiculo/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new VehiculoEntity());

        model.addAttribute("modelos", modeloService.listarTodos());

        return "vehiculo/crear";
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute VehiculoEntity vehiculo, Model modelView) {
        try {
            vehiculoService.guardarVehiculo(vehiculo);
            return "redirect:/vehiculos/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("vehiculo", vehiculo);
            modelView.addAttribute("modelos", modeloService.listarTodos());
            return "vehiculo/crear";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaVehiculo(@PathVariable Long id, Model modelView) {
        try {
            vehiculoService.darDeBaja(id);
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
        }
        return "redirect:/vehiculos/listar";
    }
}
