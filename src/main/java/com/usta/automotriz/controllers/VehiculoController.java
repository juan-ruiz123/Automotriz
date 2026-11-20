package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.VehiculoEntity;
import com.usta.automotriz.enums.TipoDocuemento;
import com.usta.automotriz.enums.TipoPlaca;
import com.usta.automotriz.models.services.ModeloService;
import com.usta.automotriz.models.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private ModeloService modeloService;

    private void cargarCatalogos(Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        model.addAttribute("tiposPlaca", TipoPlaca.values());
        model.addAttribute("tiposDocumento", TipoDocuemento.values());
    }

    @GetMapping("/listar")
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        return "vehiculo/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("vehiculo", new VehiculoEntity());
        cargarCatalogos(model);
        return "vehiculo/crear";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return vehiculoService.buscarPorId(id).map(vehiculo -> {
            model.addAttribute("vehiculo", vehiculo);
            cargarCatalogos(model);
            return "vehiculo/crear";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "El vehículo solicitado no existe.");
            return "redirect:/vehiculos/listar";
        });
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return vehiculoService.buscarPorId(id).map(vehiculo -> {
            model.addAttribute("vehiculo", vehiculo);
            return "vehiculo/detalle";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "El vehículo solicitado no existe.");
            return "redirect:/vehiculos/listar";
        });
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute VehiculoEntity vehiculo, Model modelView, RedirectAttributes flash) {
        try {
            boolean esNuevo = (vehiculo.getIdVehiculo() == null);
            vehiculoService.guardarVehiculo(vehiculo);
            flash.addFlashAttribute("exito", esNuevo ? "Vehículo registrado correctamente." : "Vehículo actualizado correctamente.");
            return "redirect:/vehiculos/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("vehiculo", vehiculo);
            cargarCatalogos(modelView);
            return "vehiculo/crear";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaVehiculo(@PathVariable Long id, RedirectAttributes flash) {
        try {
            vehiculoService.darDeBaja(id);
            flash.addFlashAttribute("exito", "Vehículo dado de baja exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vehiculos/listar";
    }
}