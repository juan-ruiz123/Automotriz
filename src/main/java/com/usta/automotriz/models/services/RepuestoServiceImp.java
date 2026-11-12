package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ModeloEntity;
import com.usta.automotriz.entities.RepuestoEntity;
import com.usta.automotriz.models.daos.ModeloDao;
import com.usta.automotriz.models.daos.RepuestoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String regexEan = "^[0-9]{13}$";
        if (repuesto.getEanRepuesto() == null || !repuesto.getEanRepuesto().matches(regexEan)) {
            throw new Exception("Error: El código EAN debe tener exactamente 13 dígitos numéricos.");
        }

        if (repuesto.getIdRepuesto() == null && repuestoDao.existsByEanRepuesto(repuesto.getEanRepuesto())) {
            throw new Exception("Error: Ya existe un repuesto con el código EAN " + repuesto.getEanRepuesto());
        }

        if (repuesto.getPrecioRepuesto() == null || repuesto.getPrecioRepuesto() <= 0) {
            throw new Exception("Error: El precio debe ser mayor a 0.");
        }
        if (repuesto.getStockRepuesto() == null || repuesto.getStockRepuesto() < 0) {
            throw new Exception("Error: El stock no puede ser negativo.");
        }

        if (repuesto.getIdRepuesto() == null) {
            repuesto.setActivoRepuesto(true);
        }

        repuestoDao.save(repuesto);
    }

    @Override
    public Optional<RepuestoEntity> buscarPorId(Long id) {
        return repuestoDao.findById(id);
    }

    @Override
    public void darDeBaja(Long id) throws Exception {
        Optional<RepuestoEntity> repuestoExistente = repuestoDao.findById(id);
        if (repuestoExistente.isPresent()) {
            RepuestoEntity repuestoUpdate = repuestoExistente.get();
            repuestoUpdate.setActivoRepuesto(false);
            repuestoDao.save(repuestoUpdate);
        } else {
            throw new Exception("Error: El repuesto no existe.");
        }
    }

    @Override
    public List<RepuestoEntity> buscarPorModelo(Long idModelo) {
        Optional<ModeloEntity> modeloOpt = modeloDao.findById(idModelo);

        if (modeloOpt.isPresent()) {
            ModeloEntity modelo = modeloOpt.get();

            if (modelo.getRepuestos() != null) {
                return modelo.getRepuestos().stream()
                        .filter(RepuestoEntity::getActivoRepuesto)
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }
}
