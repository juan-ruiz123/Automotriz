package com.usta.automotriz.controllers;

import com.usta.automotriz.enums.EstadoReparacion;
import com.usta.automotriz.models.services.ModeloService;
import com.usta.automotriz.models.services.ReparacionService;
import com.usta.automotriz.models.services.RepuestoService;
import com.usta.automotriz.models.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private RepuestoService repuestoService;

    @Autowired
    private ReparacionService reparacionService;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalModelos", modeloService.listarTodos().size());
        model.addAttribute("totalVehiculos", vehiculoService.listarTodos().size());
        model.addAttribute("totalRepuestos", repuestoService.listarTodos().size());
        var reparaciones = reparacionService.listarTodas();
        model.addAttribute("totalReparaciones", reparaciones.size());
        model.addAttribute("reparacionesEnProceso", reparaciones.stream()
                .filter(r -> r.getEstadoReparacion() == EstadoReparacion.EN_PROCESO).count());
        model.addAttribute("ultimasReparaciones", reparaciones.stream().limit(5).toList());
        return "index";
    }
}