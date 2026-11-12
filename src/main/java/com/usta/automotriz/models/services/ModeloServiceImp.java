package com.usta.automotriz.models.services;

import com.usta.automotriz.models.daos.ModeloDao;
import com.usta.automotriz.entities.ModeloEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModeloServiceImp implements ModeloService{
    @Autowired
    private ModeloDao modeloDao;

    public List<ModeloEntity> listarTodos() {
        return modeloDao.findAll();
    }

    public void guardarModelo(ModeloEntity modelo) throws Exception {
        if (modeloDao.existsByNombreModeloAndMarcaModelo(modelo.getNombreModelo(), modelo.getMarcaModelo())) {
            throw new Exception("Error: Ya existe un modelo registrado con el nombre '"
                    + modelo.getNombreModelo() + "' y la marca '" + modelo.getMarcaModelo() + "'.");
        }

        if (modelo.getIdModelo() == null) {
            modelo.setActivoModelo(true);
        }

        modeloDao.save(modelo);
    }

    public Optional<ModeloEntity> buscarPorId(Long id) {
        return modeloDao.findById(id);
    }

    public void darDeBaja(Long id) throws Exception {
        Optional<ModeloEntity> modeloExistente = modeloDao.findById(id);
        if (modeloExistente.isPresent()) {
            ModeloEntity modelo = modeloExistente.get();
            modelo.setActivoModelo(false);
            modeloDao.save(modelo);
        } else {
            throw new Exception("Error: El modelo no existe.");
        }
    }
}
