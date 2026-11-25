package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.RepuestosPrevistosModel;
import backend.model.RepuestosUtilizadosModel;
import backend.model.TrabajosOrdenModel;
import backend.repository.ITrabajosOrdenRepository;

@Service
public class ITrabajosOrdenServiceImp implements ITrabajosOrdenService {

    @Autowired
    ITrabajosOrdenRepository trabajosOrdenRepository;

    @Autowired
    IOrdenesServiciosService ordenesServiciosService;

    @Autowired
    IUsuariosService usuariosService;

    @Autowired
    IServiciosService serviciosService;

    @Autowired
    IRepuestosService repuestosService;

    @Override
    public TrabajosOrdenModel crearTrabajoOrden(TrabajosOrdenModel trabajoOrden) {
        ObjectId ordenId = trabajoOrden.getOrdenId();
        if (ordenesServiciosService.buscarOrdenServicioPorId(ordenId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + ordenId + " no existe.");
        }

        ObjectId tecnicoId = trabajoOrden.getTecnicoId();
        if (usuariosService.buscarUsuarioPorId(tecnicoId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El técnico con ID " + tecnicoId + " no existe.");
        }

        if (trabajoOrden.getServicio() != null) {
            ObjectId servicioId = trabajoOrden.getServicio().getServicioId();
            if (serviciosService.buscarServicioPorId(servicioId) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + servicioId + " no existe.");
            }
        }

        if (trabajoOrden.getRepuestosPrevistos() != null) {
            for (RepuestosPrevistosModel repuesto : trabajoOrden.getRepuestosPrevistos()) {
                if (repuestosService.buscarRepuestoPorId(repuesto.getRepuestoId()) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto previsto con ID " + repuesto.getRepuestoId() + " no existe.");
                }
            }
        }

        if (trabajoOrden.getEjecucion() != null && trabajoOrden.getEjecucion().getRepuestosUtilizados() != null) {
            for (RepuestosUtilizadosModel repuesto : trabajoOrden.getEjecucion().getRepuestosUtilizados()) {
                if (repuestosService.buscarRepuestoPorId(repuesto.getRepuestoId()) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto utilizado con ID " + repuesto.getRepuestoId() + " no existe.");
                }
            }
        }

        return trabajosOrdenRepository.save(trabajoOrden);
    }

    @Override
    public List<TrabajosOrdenModel> listarTrabajosOrden() {
        return trabajosOrdenRepository.findAll();
    }

    @Override
    public TrabajosOrdenModel buscarTrabajoOrdenPorId(ObjectId id) {
        return trabajosOrdenRepository.findById(id).orElse(null);
    }
}