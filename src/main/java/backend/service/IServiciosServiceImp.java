package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.model.ServiciosModel;
import backend.repository.IServiciosRepository;

@Service
public class IServiciosServiceImp implements IServiciosService {

    @Autowired
    IServiciosRepository serviciosRepository;

    @Override
    public ServiciosModel crearServicio(ServiciosModel servicio) {
        return serviciosRepository.save(servicio);
    }

    @Override
    public List<ServiciosModel> listarServicios() {
        return serviciosRepository.findAll();
    }

    @Override
    public ServiciosModel buscarServicioPorId(ObjectId id) {
        return serviciosRepository.findById(id).orElse(null);
    }
}