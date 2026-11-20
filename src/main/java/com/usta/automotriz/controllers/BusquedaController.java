package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.ModeloEntity;
import com.usta.automotriz.entities.RepuestoEntity;
import com.usta.automotriz.entities.VehiculoEntity;
import com.usta.automotriz.models.services.ModeloService;
import com.usta.automotriz.models.services.RepuestoService;
import com.usta.automotriz.models.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/busquedas")
public class BusquedaController {

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private RepuestoService repuestoService;

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public String mostrarBusquedas(Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        return "busquedas/index";
    }

    // B1: Repuestos compatibles con un modelo
    @GetMapping("/repuestos-por-modelo")
    public String buscarRepuestosPorModelo(@RequestParam(value = "idModelo", required = false) Long idModelo, Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        model.addAttribute("modeloSeleccionadoId", idModelo);

        if (idModelo != null) {
            Optional<ModeloEntity> modeloOpt = modeloService.buscarPorId(idModelo);
            modeloOpt.ifPresent(m -> model.addAttribute("modeloConsultado", m));

            List<RepuestoEntity> compatibles = repuestoService.buscarPorModelo(idModelo);
            model.addAttribute("repuestosCompatibles", compatibles);
            if (compatibles.isEmpty()) {
                model.addAttribute("mensajeB1", "No hay repuestos compatibles con este modelo");
            }
        }
        return "busquedas/index";
    }

    // B2: Modelo por placa (valida formato ANTES de consultar BD)
    @GetMapping("/modelo-por-placa")
    public String buscarModeloPorPlaca(@RequestParam(value = "placa", required = false) String placa, Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        model.addAttribute("placaBuscada", placa);

        if (placa != null) {
            String placaNormalizada = placa.trim().toUpperCase();
            if (!placaNormalizada.matches("^[A-Z]{3}-[0-9]{3}$")) {
                model.addAttribute("errorB2", "Formato de placa inválido. Debe cumplir el formato ABC-123.");
                return "busquedas/index";
            }

            Optional<VehiculoEntity> vehiculoOpt = vehiculoService.buscarPorPlaca(placaNormalizada);
            if (vehiculoOpt.isEmpty()) {
                model.addAttribute("mensajeB2", "Placa no registrada");
            } else {
                model.addAttribute("vehiculoEncontrado", vehiculoOpt.get());
            }
        }
        return "busquedas/index";
    }
}