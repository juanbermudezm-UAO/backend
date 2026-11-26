package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.RepuestosModel;
import backend.repository.IRepuestosRepository;

@Service
public class IRepuestosServiceImp implements IRepuestosService {

    @Autowired
    IRepuestosRepository repuestosRepository;

    @Override
    public RepuestosModel crearRepuesto(RepuestosModel repuesto) {
        return repuestosRepository.save(repuesto);
    }

    @Override
    public List<RepuestosModel> listarRepuestos() {
        return repuestosRepository.findAll();
    }

    @Override
    public RepuestosModel buscarRepuestoPorId(ObjectId id) {
        return repuestosRepository.findById(id).orElse(null);
    }

    @Override
    public RepuestosModel actualizarRepuesto(ObjectId id, RepuestosModel repuesto) {
        if (buscarRepuestoPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto con ID " + id + " no existe.");
        }
        repuesto.setId(id);
        return repuestosRepository.save(repuesto);
    }

    @Override
    public void eliminarRepuesto(ObjectId id) {
        if (buscarRepuestoPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto con ID " + id + " no existe.");
        }
        repuestosRepository.deleteById(id);
    }
}