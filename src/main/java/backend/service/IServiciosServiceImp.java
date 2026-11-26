package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public ServiciosModel actualizarServicio(ObjectId id, ServiciosModel servicio) {
        if (buscarServicioPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + id + " no existe.");
        }
        servicio.setId(id);
        return serviciosRepository.save(servicio);
    }

    @Override
    public void eliminarServicio(ObjectId id) {
        if (buscarServicioPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + id + " no existe.");
        }
        serviciosRepository.deleteById(id);
    }
}