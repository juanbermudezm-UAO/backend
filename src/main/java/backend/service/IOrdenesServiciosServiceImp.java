package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.OrdenesServiciosModel;
import backend.repository.IOrdenesServiciosRepository;

@Service
public class IOrdenesServiciosServiceImp implements IOrdenesServiciosService {

    @Autowired
    IOrdenesServiciosRepository ordenesServiciosRepository;

    @Autowired
    IUsuariosService usuariosService;

    @Autowired
    IBicicletasService bicicletasService;

    @Autowired
    ICitasService citasService;

    private void validarReferencias(OrdenesServiciosModel ordenServicio) {
        ObjectId clienteId = ordenServicio.getClienteId();
        if (usuariosService.buscarUsuarioPorId(clienteId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente con ID " + clienteId + " no existe.");
        }

        if (ordenServicio.getBicicleta() != null) {
            ObjectId bicicletaId = ordenServicio.getBicicleta().getBicicletaId();
            if (bicicletasService.buscarBicicletaPorId(bicicletaId) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe.");
            }
        }

        ObjectId citaId = ordenServicio.getCitaId();
        if (citaId != null && citasService.buscarCitaPorId(citaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + citaId + " no existe.");
        }

        ObjectId coordinadorId = ordenServicio.getCoordinadorId();
        if (usuariosService.buscarUsuarioPorId(coordinadorId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El coordinador con ID " + coordinadorId + " no existe.");
        }

        if (ordenServicio.getTecnicosAsignados() != null) {
            for (ObjectId tecnicoId : ordenServicio.getTecnicosAsignados()) {
                if (usuariosService.buscarUsuarioPorId(tecnicoId) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El técnico con ID " + tecnicoId + " no existe.");
                }
            }
        }
    }

    @Override
    public OrdenesServiciosModel crearOrdenServicio(OrdenesServiciosModel ordenServicio) {
        validarReferencias(ordenServicio);
        return ordenesServiciosRepository.save(ordenServicio);
    }

    @Override
    public List<OrdenesServiciosModel> listarOrdenesServicios() {
        return ordenesServiciosRepository.findAll();
    }

    @Override
    public OrdenesServiciosModel buscarOrdenServicioPorId(ObjectId id) {
        return ordenesServiciosRepository.findById(id).orElse(null);
    }

    @Override
    public OrdenesServiciosModel actualizarOrdenServicio(ObjectId id, OrdenesServiciosModel ordenServicio) {
        if (buscarOrdenServicioPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + id + " no existe.");
        }
        validarReferencias(ordenServicio);
        ordenServicio.setId(id);
        return ordenesServiciosRepository.save(ordenServicio);
    }

    @Override
    public void eliminarOrdenServicio(ObjectId id) {
        if (buscarOrdenServicioPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + id + " no existe.");
        }
        ordenesServiciosRepository.deleteById(id);
    }
}