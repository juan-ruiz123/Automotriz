package com.usta.automotriz.controllers;

import com.usta.automotriz.entities.ReparacionEntity;
import com.usta.automotriz.entities.UsuarioEntity;
import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.enums.TipoReparacion;
import com.usta.automotriz.models.services.ReparacionService;
import com.usta.automotriz.models.services.UsuarioService;
import com.usta.automotriz.models.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reparaciones")
public class ReparacionController {

    @Autowired
    private ReparacionService reparacionService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private UsuarioService usuarioService;

    private boolean esAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMINISTRADOR"));
    }

    private void cargarCatalogos(Model model, Authentication auth) {
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("tiposReparacion", TipoReparacion.values());
        if (esAdmin(auth)) {
            model.addAttribute("mecanicos", usuarioService.listarMecanicos());
        } else {
            UsuarioEntity actual = usuarioService.buscarPorUsername(auth.getName()).orElse(null);
            model.addAttribute("mecanicos", actual != null ? List.of(actual) : List.of());
        }
    }

    @GetMapping("/listar")
    public String listarReparaciones(Model model, Authentication auth) {
        if (esAdmin(auth)) {
            model.addAttribute("reparaciones", reparacionService.listarTodas());
        } else {
            model.addAttribute("reparaciones", reparacionService.listarPorMecanicoUsername(auth.getName()));
        }
        return "reparacion/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model, Authentication auth) {
        ReparacionEntity rep = new ReparacionEntity();
        if (!esAdmin(auth)) {
            usuarioService.buscarPorUsername(auth.getName()).ifPresent(rep::setIdUsuario);
        }
        model.addAttribute("reparacion", rep);
        cargarCatalogos(model, auth);
        return "reparacion/crear";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, Authentication auth, RedirectAttributes flash) {
        return reparacionService.buscarPorId(id).map(rep -> {
            if (!esAdmin(auth) && !rep.getIdUsuario().getUserNameUsuario().equals(auth.getName())) {
                flash.addFlashAttribute("error", "Solo puedes editar tus propias reparaciones.");
                return "redirect:/reparaciones/listar";
            }
            model.addAttribute("reparacion", rep);
            cargarCatalogos(model, auth);
            return "reparacion/crear";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "La reparación solicitada no existe.");
            return "redirect:/reparaciones/listar";
        });
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, Authentication auth, RedirectAttributes flash) {
        return reparacionService.buscarPorId(id).map(rep -> {
            if (!esAdmin(auth) && !rep.getIdUsuario().getUserNameUsuario().equals(auth.getName())) {
                flash.addFlashAttribute("error", "No tienes permiso para ver esta reparación.");
                return "redirect:/reparaciones/listar";
            }
            model.addAttribute("reparacion", rep);
            return "reparacion/detalle";
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "La reparación solicitada no existe.");
            return "redirect:/reparaciones/listar";
        });
    }

    @PostMapping("/guardar")
    public String guardarReparacion(@ModelAttribute ReparacionEntity reparacion,
                                    Model modelView,
                                    Authentication auth,
                                    RedirectAttributes flash) {
        try {
            if (!esAdmin(auth)) {
                UsuarioEntity actual = usuarioService.buscarPorUsername(auth.getName())
                        .orElseThrow(() -> new Exception("Usuario no encontrado."));
                reparacion.setIdUsuario(actual);
            }
            boolean esNuevo = (reparacion.getIdReparacion() == null);
            reparacionService.guardarReparacion(reparacion);
            flash.addFlashAttribute("exito", esNuevo ? "Reparación iniciada en estado EN PROCESO." : "Reparación actualizada.");
            return "redirect:/reparaciones/listar";
        } catch (Exception e) {
            modelView.addAttribute("error", e.getMessage());
            modelView.addAttribute("reparacion", reparacion);
            cargarCatalogos(modelView, auth);
            return "reparacion/crear";
        }
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam("nuevoEstado") String nuevoEstado,
                                Authentication auth,
                                RedirectAttributes flash) {
        try {
            ReparacionEntity rep = reparacionService.buscarPorId(id)
                    .orElseThrow(() -> new Exception("La reparación no existe."));

            if (!esAdmin(auth) && !rep.getIdUsuario().getUserNameUsuario().equals(auth.getName())) {
                flash.addFlashAttribute("error", "Solo puedes cambiar el estado de tus propias reparaciones.");
                return "redirect:/reparaciones/listar";
            }

            // Solo el administrador puede cancelar desde esta vía (Sección 3.2)
            if (!esAdmin(auth) && "CANCELADO".equals(nuevoEstado)) {
                flash.addFlashAttribute("error", "Solo el administrador puede cancelar reparaciones.");
                return "redirect:/reparaciones/listar";
            }

            reparacionService.cambiarEstado(id, nuevoEstado);
            if ("TERMINADO".equals(nuevoEstado)) {
                flash.addFlashAttribute("exito", "Reparación marcada como TERMINADA y notificación enviada al correo del dueño.");
            } else {
                flash.addFlashAttribute("exito", "Estado actualizado correctamente.");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reparaciones/listar";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarReparacion(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reparacionService.cancelarReparacion(id);
            flash.addFlashAttribute("exito", "Reparación cancelada.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reparaciones/listar";
    }
}