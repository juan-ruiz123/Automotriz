package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.ModeloEntity;
import com.usta.automotriz.entities.RepuestoEntity;
import com.usta.automotriz.models.services.ModeloService;
import com.usta.automotriz.models.services.RepuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("repuesto", new RepuestoEntity());
        model.addAttribute("listaModelos", modeloService.listarTodos());
        model.addAttribute("modelosSeleccionados", List.of());
        return "repuesto/crear";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return repuestoService.buscarPorId(id).map(repuesto -> {
            List<Long> seleccionados = repuestoService.obtenerModelosCompatibles(id).stream()
                    .map(ModeloEntity::getIdModelo)
                    .toList();
            model.addAttribute("repuesto", repuesto);
            model.addAttribute("listaModelos", modeloService.listarTodos());
            model.addAttribute("modelosSeleccionados", seleccionados);
            return "repuesto/crear";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "El repuesto solicitado no existe.");
            return "redirect:/repuestos/listar";
        });
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return repuestoService.buscarPorId(id).map(repuesto -> {
            model.addAttribute("repuesto", repuesto);
            model.addAttribute("modelosCompatibles", repuestoService.obtenerModelosCompatibles(id));
            return "repuesto/detalle";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "El repuesto solicitado no existe.");
            return "redirect:/repuestos/listar";
        });
    }

    @PostMapping("/guardar")
    public String guardarRepuesto(@ModelAttribute RepuestoEntity repuesto,
                                  @RequestParam(value = "modelosIds", required = false) List<Long> modelosIds,
                                  Model modelView,
                                  RedirectAttributes flash) {
        try {
            boolean esNuevo = (repuesto.getIdRepuesto() == null);
            repuestoService.guardarRepuestoConModelos(repuesto, modelosIds);
            flash.addFlashAttribute("exito", esNuevo ? "Repuesto registrado correctamente." : "Repuesto actualizado correctamente.");
            return "redirect:/repuestos/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("repuesto", repuesto);
            modelView.addAttribute("listaModelos", modeloService.listarTodos());
            modelView.addAttribute("modelosSeleccionados", modelosIds != null ? modelosIds : List.of());
            return "repuesto/crear";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaRepuesto(@PathVariable Long id, RedirectAttributes flash) {
        try {
            repuestoService.darDeBaja(id);
            flash.addFlashAttribute("exito", "Repuesto dado de baja exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/repuestos/listar";
    }
}