package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.BicicletasModel;
import backend.model.CitasModel;
import backend.model.UsuariosModel;
import backend.repository.ICitasRepository;

@Service
public class ICitasServiceImp implements ICitasService {

    private static final List<String> ESTADOS_VALIDOS = List.of("solicitada", "confirmada", "atendida", "cancelada");
    private static final List<String> ESTADOS_ACTIVOS = List.of("solicitada", "confirmada");

    @Autowired
    ICitasRepository citasRepository;

    @Autowired
    IUsuariosService usuariosService;

    @Autowired
    IBicicletasService bicicletasService;

    @Autowired
    IServiciosService serviciosService;

    private void validarCita(CitasModel cita, CitasModel citaAnterior) {
        if (cita.getClienteId() == null || cita.getBicicletaId() == null || cita.getFechaHora() == null
                || cita.getMotivoVisita() == null || cita.getMotivoVisita().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clienteId, bicicletaId, fechaHora y motivoVisita son obligatorios.");
        }
        if (cita.getEstado() == null || !ESTADOS_VALIDOS.contains(cita.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado de la cita debe ser uno de: " + ESTADOS_VALIDOS);
        }

        UsuariosModel cliente = usuariosService.buscarUsuarioPorId(cita.getClienteId());
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente con ID " + cita.getClienteId() + " no existe.");
        }
        if (!"cliente".equals(cliente.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario asociado a la cita debe tener rol 'cliente'.");
        }

        BicicletasModel bicicleta = bicicletasService.buscarBicicletaPorId(cita.getBicicletaId());
        if (bicicleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + cita.getBicicletaId() + " no existe.");
        }
        if (bicicleta.getPropietarioId() != null && !bicicleta.getPropietarioId().equals(cita.getClienteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La bicicleta indicada no pertenece al cliente de la cita.");
        }

        if (cita.getServiciosInteres() != null) {
            for (ObjectId servicioId : cita.getServiciosInteres()) {
                if (serviciosService.buscarServicioPorId(servicioId) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + servicioId + " no existe.");
                }
            }
        }

        if (ESTADOS_ACTIVOS.contains(cita.getEstado())) {
            List<CitasModel> duplicadas = citasRepository.findByBicicletaIdAndFechaHoraAndEstadoIn(
                    cita.getBicicletaId(), cita.getFechaHora(), ESTADOS_ACTIVOS);
            boolean hayConflicto = duplicadas.stream()
                    .anyMatch(c -> citaAnterior == null || !c.getId().equals(citaAnterior.getId()));
            if (hayConflicto) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cita activa para esta bicicleta en el mismo horario.");
            }
        }

        if (citaAnterior != null && !citaAnterior.getFechaHora().equals(cita.getFechaHora())) {
            if (cita.getObservacionCambioHorario() == null || cita.getObservacionCambioHorario().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe registrar observacionCambioHorario al modificar la fecha/hora de la cita.");
            }
        }
    }

    @Override
    public CitasModel crearCita(CitasModel cita) {
        validarCita(cita, null);
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
        CitasModel citaAnterior = buscarCitaPorId(id);
        if (citaAnterior == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + id + " no existe.");
        }
        validarCita(cita, citaAnterior);
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