package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.SolicitudesGarantiasModel;
import backend.repository.ISolicitudesGarantiasRepository;

@Service
public class ISolicitudesGarantiasServiceImp implements ISolicitudesGarantiasService {

    @Autowired
    ISolicitudesGarantiasRepository solicitudesGarantiasRepository;

    @Autowired
    ITrabajosOrdenService trabajosOrdenService;

    @Override
    public SolicitudesGarantiasModel crearSolicitudGarantia(SolicitudesGarantiasModel solicitudGarantia) {
        ObjectId trabajoId = solicitudGarantia.getTrabajoId();
        if (trabajosOrdenService.buscarTrabajoOrdenPorId(trabajoId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo de orden con ID " + trabajoId + " no existe.");
        }

        return solicitudesGarantiasRepository.save(solicitudGarantia);
    }

    @Override
    public List<SolicitudesGarantiasModel> listarSolicitudesGarantias() {
        return solicitudesGarantiasRepository.findAll();
    }

    @Override
    public SolicitudesGarantiasModel buscarSolicitudGarantiaPorId(ObjectId id) {
        return solicitudesGarantiasRepository.findById(id).orElse(null);
    }
}