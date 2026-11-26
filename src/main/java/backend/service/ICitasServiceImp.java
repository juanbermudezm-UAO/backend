package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.CitasModel;
import backend.repository.ICitasRepository;

@Service
public class ICitasServiceImp implements ICitasService {

    @Autowired
    ICitasRepository citasRepository;

    @Autowired
    IUsuariosService usuariosService;

    @Autowired
    IBicicletasService bicicletasService;

    @Autowired
    IServiciosService serviciosService;

    private void validarReferencias(CitasModel cita) {
        ObjectId clienteId = cita.getClienteId();
        if (usuariosService.buscarUsuarioPorId(clienteId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente con ID " + clienteId + " no existe.");
        }

        ObjectId bicicletaId = cita.getBicicletaId();
        if (bicicletasService.buscarBicicletaPorId(bicicletaId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe.");
        }

        if (cita.getServiciosInteres() != null) {
            for (ObjectId servicioId : cita.getServiciosInteres()) {
                if (serviciosService.buscarServicioPorId(servicioId) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + servicioId + " no existe.");
                }
            }
        }
    }

    @Override
    public CitasModel crearCita(CitasModel cita) {
        validarReferencias(cita);
        return citasRepository.save(cita);
    }

    @Override
    public List<CitasModel> listarCitas() {
        return citasRepository.findAll();
    }

    @Override
    public CitasModel buscarCitaPorId(ObjectId id) {
        return citasRepository.findById(id).orElse(null);
    }

    @Override
    public CitasModel actualizarCita(ObjectId id, CitasModel cita) {
        if (buscarCitaPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + id + " no existe.");
        }
        validarReferencias(cita);
        cita.setId(id);
        return citasRepository.save(cita);
    }

    @Override
    public void eliminarCita(ObjectId id) {
        if (buscarCitaPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + id + " no existe.");
        }
        citasRepository.deleteById(id);
    }
}