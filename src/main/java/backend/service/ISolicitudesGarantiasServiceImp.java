package backend.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.Garantia;
import backend.model.SolicitudesGarantiasModel;
import backend.model.TrabajosOrdenModel;
import backend.repository.ISolicitudesGarantiasRepository;

@Service
public class ISolicitudesGarantiasServiceImp implements ISolicitudesGarantiasService {

    private static final List<String> ESTADOS_VALIDOS = List.of("registrada", "en_revision", "cerrada");

    @Autowired
    ISolicitudesGarantiasRepository solicitudesGarantiasRepository;

    @Autowired
    ITrabajosOrdenService trabajosOrdenService;

    private void validarSolicitudGarantia(SolicitudesGarantiasModel solicitud) {
        if (solicitud.getTrabajoId() == null || solicitud.getDescripcion() == null || solicitud.getDescripcion().isBlank()
                || solicitud.getFecha() == null || solicitud.getEvidencias() == null || solicitud.getEvidencias().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "trabajoId, descripcion, fecha y evidencias son obligatorios.");
        }
        if (solicitud.getEstado() == null || !ESTADOS_VALIDOS.contains(solicitud.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado de la solicitud debe ser uno de: " + ESTADOS_VALIDOS);
        }

        TrabajosOrdenModel trabajo = trabajosOrdenService.buscarTrabajoOrdenPorId(solicitud.getTrabajoId());
        if (trabajo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo de orden con ID " + solicitud.getTrabajoId() + " no existe.");
        }

        Garantia garantia = trabajo.getGarantia();
        if (garantia == null || garantia.getFechaInicio() == null || garantia.getDuracion() == null || garantia.getDuracion() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El trabajo asociado no cuenta con una garantía válida.");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(garantia.getFechaInicio());
        cal.add(Calendar.DAY_OF_YEAR, garantia.getDuracion());
        Date fechaVencimiento = cal.getTime();

        if (solicitud.getFecha().before(garantia.getFechaInicio()) || solicitud.getFecha().after(fechaVencimiento)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud está fuera del periodo de vigencia de la garantía.");
        }
    }

    @Override
    public SolicitudesGarantiasModel crearSolicitudGarantia(SolicitudesGarantiasModel solicitudGarantia) {
        validarSolicitudGarantia(solicitudGarantia);
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

    @Override
    public SolicitudesGarantiasModel actualizarSolicitudGarantia(ObjectId id, SolicitudesGarantiasModel solicitudGarantia) {
        if (buscarSolicitudGarantiaPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La solicitud de garantía con ID " + id + " no existe.");
        }
        validarSolicitudGarantia(solicitudGarantia);
        solicitudGarantia.setId(id);
        return solicitudesGarantiasRepository.save(solicitudGarantia);
    }

    @Override
    public void eliminarSolicitudGarantia(ObjectId id) {
        if (buscarSolicitudGarantiaPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La solicitud de garantía con ID " + id + " no existe.");
        }
        solicitudesGarantiasRepository.deleteById(id);
    }
}