package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ModeloEntity;
import com.usta.automotriz.models.daos.ModeloDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModeloServiceImp implements ModeloService {

    @Autowired
    private ModeloDao modeloDao;

    @Override
    public List<ModeloEntity> listarTodos() {
        return modeloDao.findAll();
    }

    @Override
    public void guardarModelo(ModeloEntity modelo) throws Exception {
        if (modelo.getIdModelo() == null) {
            if (modeloDao.existsByNombreModeloAndMarcaModelo(modelo.getNombreModelo(), modelo.getMarcaModelo())) {
                throw new Exception("Ya existe un modelo registrado con el nombre '"
                        + modelo.getNombreModelo() + "' y la marca '" + modelo.getMarcaModelo() + "'.");
            }
            modelo.setActivoModelo(true);
        } else {
            ModeloEntity existente = modeloDao.findById(modelo.getIdModelo())
                    .orElseThrow(() -> new Exception("El modelo no existe."));

            boolean cambioNombreOMarca = !existente.getNombreModelo().equalsIgnoreCase(modelo.getNombreModelo())
                    || !existente.getMarcaModelo().equalsIgnoreCase(modelo.getMarcaModelo());

            if (cambioNombreOMarca && modeloDao.existsByNombreModeloAndMarcaModelo(modelo.getNombreModelo(), modelo.getMarcaModelo())) {
                throw new Exception("Ya existe otro modelo con el nombre '"
                        + modelo.getNombreModelo() + "' y la marca '" + modelo.getMarcaModelo() + "'.");
            }
            modelo.setActivoModelo(existente.getActivoModelo());
            modelo.setRepuestos(existente.getRepuestos());
        }
        modeloDao.save(modelo);
    }

    @Override
    public Optional<ModeloEntity> buscarPorId(Long id) {
        return modeloDao.findById(id);
    }

    @Override
    public void darDeBaja(Long id) throws Exception {
        ModeloEntity modelo = modeloDao.findById(id)
                .orElseThrow(() -> new Exception("El modelo no existe."));
        modelo.setActivoModelo(false);
        modeloDao.save(modelo);
    }
}