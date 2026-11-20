package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ModeloEntity;
import com.usta.automotriz.entities.RepuestoEntity;
import com.usta.automotriz.models.daos.ModeloDao;
import com.usta.automotriz.models.daos.RepuestoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepuestoServiceImp implements RepuestoService {

    @Autowired
    private RepuestoDao repuestoDao;

    @Autowired
    private ModeloDao modeloDao;

    @Override
    public List<RepuestoEntity> listarTodos() {
        return repuestoDao.findAll();
    }

    @Override
    public void guardarRepuesto(RepuestoEntity repuesto) throws Exception {
        validarDatosRepuesto(repuesto);
        repuestoDao.save(repuesto);
    }

    @Override
    @Transactional
    public void guardarRepuestoConModelos(RepuestoEntity repuesto, List<Long> modelosIds) throws Exception {
        if (modelosIds == null || modelosIds.isEmpty()) {
            throw new Exception("Debe seleccionar al menos un modelo compatible para el repuesto.");
        }

        validarDatosRepuesto(repuesto);
        RepuestoEntity guardado = repuestoDao.save(repuesto);

        // Sincronizar la relación muchos a muchos desde ModeloEntity
        List<ModeloEntity> todosLosModelos = modeloDao.findAll();
        for (ModeloEntity modelo : todosLosModelos) {
            boolean debeEstar = modelosIds.contains(modelo.getIdModelo());
            boolean yaEsta = modelo.getRepuestos().stream()
                    .anyMatch(r -> r.getIdRepuesto().equals(guardado.getIdRepuesto()));

            if (debeEstar && !yaEsta) {
                modelo.getRepuestos().add(guardado);
                modeloDao.save(modelo);
            } else if (!debeEstar && yaEsta) {
                modelo.getRepuestos().removeIf(r -> r.getIdRepuesto().equals(guardado.getIdRepuesto()));
                modeloDao.save(modelo);
            }
        }
    }

    private void validarDatosRepuesto(RepuestoEntity repuesto) throws Exception {
        // RN-05: EAN 13 dígitos
        String regexEan = "^[0-9]{13}$";
        if (repuesto.getEanRepuesto() == null || !repuesto.getEanRepuesto().matches(regexEan)) {
            throw new Exception("El código EAN debe tener exactamente 13 dígitos numéricos.");
        }

        // RN-06: Precio > 0 y Stock >= 0
        if (repuesto.getPrecioRepuesto() == null || repuesto.getPrecioRepuesto() <= 0) {
            throw new Exception("El precio debe ser mayor a 0.");
        }
        if (repuesto.getStockRepuesto() == null || repuesto.getStockRepuesto() < 0) {
            throw new Exception("El stock no puede ser negativo.");
        }

        // Imagen por defecto si viene vacía (evita fallo por @NotBlank en entidad)
        if (repuesto.getFotoRepuesto() == null || repuesto.getFotoRepuesto().isBlank()) {
            repuesto.setFotoRepuesto("https://cdn-icons-png.flaticon.com/512/1518/1518965.png");
        }

        if (repuesto.getIdRepuesto() == null) {
            if (repuestoDao.existsByEanRepuesto(repuesto.getEanRepuesto())) {
                throw new Exception("Ya existe un repuesto con el código EAN " + repuesto.getEanRepuesto());
            }
            repuesto.setActivoRepuesto(true);
        } else {
            RepuestoEntity existente = repuestoDao.findById(repuesto.getIdRepuesto())
                    .orElseThrow(() -> new Exception("El repuesto no existe."));

            if (!existente.getEanRepuesto().equals(repuesto.getEanRepuesto())
                    && repuestoDao.existsByEanRepuesto(repuesto.getEanRepuesto())) {
                throw new Exception("El código EAN " + repuesto.getEanRepuesto() + " ya pertenece a otro repuesto.");
            }
            repuesto.setActivoRepuesto(existente.getActivoRepuesto());
        }
    }

    @Override
    public Optional<RepuestoEntity> buscarPorId(Long id) {
        return repuestoDao.findById(id);
    }

    @Override
    public void darDeBaja(Long id) throws Exception {
        RepuestoEntity repuesto = repuestoDao.findById(id)
                .orElseThrow(() -> new Exception("El repuesto no existe."));
        repuesto.setActivoRepuesto(false);
        repuestoDao.save(repuesto);
    }

    @Override
    public List<RepuestoEntity> buscarPorModelo(Long idModelo) {
        return modeloDao.findById(idModelo)
                .map(modelo -> modelo.getRepuestos().stream()
                        .filter(RepuestoEntity::getActivoRepuesto)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Override
    public List<ModeloEntity> obtenerModelosCompatibles(Long idRepuesto) {
        return modeloDao.findAll().stream()
                .filter(m -> m.getRepuestos().stream().anyMatch(r -> r.getIdRepuesto().equals(idRepuesto)))
                .collect(Collectors.toList());
    }
}