package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}