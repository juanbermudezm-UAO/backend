package backend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.BicicletasModel;
import backend.model.CitasModel;
import backend.model.EvidenciasHallazgoModel;
import backend.model.HallazgosModel;
import backend.model.HistorialEstadosModel;
import backend.model.OrdenesServiciosModel;
import backend.model.RepuestosPrevistosModel;
import backend.model.TrabajosOrdenModel;
import backend.model.UsuariosModel;
import backend.repository.IOrdenesServiciosRepository;
import backend.repository.ITrabajosOrdenRepository;

@Service
public class IOrdenesServiciosServiceImp implements IOrdenesServiciosService {

    private static final List<String> ESTADOS_VALIDOS = List.of(
            "recibida",
            "en_diagnostico",
            "pendiente_autorizacion",
            "en_ejecucion",
            "lista_para_entrega",
            "entregada",
            "cancelada"
    );

    private static final List<String> ESTADOS_FINALES = List.of("entregada", "cancelada");

    @Autowired
    IOrdenesServiciosRepository ordenesServiciosRepository;

    @Autowired
    ITrabajosOrdenRepository trabajosOrdenRepository;

    @Autowired
    IUsuariosService usuariosService;

    @Autowired
    IBicicletasService bicicletasService;

    @Autowired
    ICitasService citasService;

    private void validarOrden(OrdenesServiciosModel orden, OrdenesServiciosModel ordenAnterior) {
        if (orden.getClienteId() == null || orden.getBicicleta() == null || orden.getBicicleta().getBicicletaId() == null
                || orden.getFechaRecepcion() == null || orden.getFechaEstimadaEntrega() == null
                || orden.getCoordinadorId() == null || orden.getTecnicosAsignados() == null || orden.getTecnicosAsignados().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clienteId, bicicleta, fechaRecepcion, fechaEstimadaEntrega, coordinadorId y tecnicosAsignados son obligatorios.");
        }
        if (orden.getEstado() == null || !ESTADOS_VALIDOS.contains(orden.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado de la orden debe ser uno de: " + ESTADOS_VALIDOS);
        }

        // Validar rol cliente
        UsuariosModel cliente = usuariosService.buscarUsuarioPorId(orden.getClienteId());
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente con ID " + orden.getClienteId() + " no existe.");
        }
        if (!"cliente".equals(cliente.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario clienteId debe tener rol 'cliente'.");
        }

        // Validar bicicleta y pertenencia
        ObjectId bicicletaId = orden.getBicicleta().getBicicletaId();
        BicicletasModel bicicleta = bicicletasService.buscarBicicletaPorId(bicicletaId);
        if (bicicleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + bicicletaId + " no existe.");
        }
        if (bicicleta.getPropietarioId() != null && !bicicleta.getPropietarioId().equals(orden.getClienteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La bicicleta no pertenece al cliente indicado.");
        }

        // Validar única orden activa por bicicleta
        if (!ESTADOS_FINALES.contains(orden.getEstado())) {
            List<OrdenesServiciosModel> ordenesActivas = ordenesServiciosRepository
                    .findByBicicleta_BicicletaIdAndEstadoNotIn(bicicletaId, ESTADOS_FINALES);
            boolean hayOtraActiva = ordenesActivas.stream()
                    .anyMatch(o -> ordenAnterior == null || !o.getId().equals(ordenAnterior.getId()));
            if (hayOtraActiva) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La bicicleta ya tiene una orden de servicio activa.");
            }
        }

        // Validar rol coordinador
        UsuariosModel coordinador = usuariosService.buscarUsuarioPorId(orden.getCoordinadorId());
        if (coordinador == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El coordinador con ID " + orden.getCoordinadorId() + " no existe.");
        }
        if (!"coordinador".equals(coordinador.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario coordinadorId debe tener rol 'coordinador'.");
        }

        // Validar roles de técnicos asignados
        for (ObjectId tecnicoId : orden.getTecnicosAsignados()) {
            UsuariosModel tecnico = usuariosService.buscarUsuarioPorId(tecnicoId);
            if (tecnico == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El técnico con ID " + tecnicoId + " no existe.");
            }
            if (!"tecnico".equals(tecnico.getRol())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario con ID " + tecnicoId + " no tiene rol 'tecnico'.");
            }
        }

        // Validar cita si está vinculada
        if (orden.getCitaId() != null) {
            CitasModel cita = citasService.buscarCitaPorId(orden.getCitaId());
            if (cita == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + orden.getCitaId() + " no existe.");
            }
            if (!cita.getClienteId().equals(orden.getClienteId()) || !cita.getBicicletaId().equals(bicicletaId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cita no corresponde al cliente o bicicleta de la orden.");
            }
            if (ordenAnterior == null && ("cancelada".equals(cita.getEstado()) || "atendida".equals(cita.getEstado()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cita vinculada ya fue atendida o está cancelada.");
            }
        }

        // Validar diagnóstico, técnicos de hallazgos y evidencias
        if (orden.getDiagnostico() != null && orden.getDiagnostico().getHallazgos() != null) {
            for (HallazgosModel hallazgo : orden.getDiagnostico().getHallazgos()) {
                UsuariosModel tecHallazgo = usuariosService.buscarUsuarioPorId(hallazgo.getTecnicoId());
                if (tecHallazgo == null || !"tecnico".equals(tecHallazgo.getRol())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada hallazgo debe asociarse a un usuario válido con rol 'tecnico'.");
                }
                if (hallazgo.getEvidencias() != null) {
                    for (EvidenciasHallazgoModel ev : hallazgo.getEvidencias()) {
                        if (ev.getNombreArchivo() == null || ev.getNombreArchivo().isBlank()
                                || ev.getDescripcion() == null || ev.getDescripcion().isBlank()
                                || ev.getUbicacion() == null || ev.getUbicacion().isBlank()) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada evidencia de hallazgo requiere nombreArchivo, descripcion y ubicacion.");
                        }
                    }
                }
            }
        }

        // Validar condición para pasar a lista_para_entrega
        if ("lista_para_entrega".equals(orden.getEstado()) && ordenAnterior != null) {
            List<TrabajosOrdenModel> trabajos = trabajosOrdenRepository.findByOrdenId(ordenAnterior.getId());
            List<TrabajosOrdenModel> autorizados = trabajos.stream()
                    .filter(t -> t.getAutorizacion() != null && "autorizado".equalsIgnoreCase(t.getAutorizacion().getDecision()))
                    .toList();
            if (autorizados.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay trabajos autorizados registrados para marcar la orden como lista_para_entrega.");
            }
            for (TrabajosOrdenModel t : autorizados) {
                boolean finalizado = t.getEjecucion() != null && "finalizado".equalsIgnoreCase(t.getEjecucion().getEstado());
                boolean revisadoOk = t.getRevision() != null && Boolean.TRUE.equals(t.getRevision().getAprobada());
                if (!finalizado || !revisadoOk) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los trabajos autorizados deben estar finalizados y con revisión aprobada.");
                }
            }
        }

        // Validar entrega
        if ("entregada".equals(orden.getEstado())) {
            if (orden.getEntrega() == null || orden.getEntrega().getFecha() == null
                    || !Boolean.TRUE.equals(orden.getEntrega().getConfirmacionRecepcionCliente())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para marcar como entregada se requiere fecha de entrega y confirmacionRecepcionCliente = true.");
            }
        }

        // Validar valoración (solo en órdenes entregadas, calificación 1-5 y una sola vez)
        if (orden.getValoracion() != null) {
            if (!"entregada".equals(orden.getEstado())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se puede valorar una orden en estado 'entregada'.");
            }
            Integer cal = orden.getValoracion().getCalificacion();
            if (cal == null || cal < 1 || cal > 5) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calificación debe estar entre 1 y 5.");
            }
            if (ordenAnterior != null && ordenAnterior.getValoracion() != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta orden ya cuenta con una valoración registrada.");
            }
        }
    }

    private void actualizarHistorialEstados(OrdenesServiciosModel orden, OrdenesServiciosModel ordenAnterior) {
        List<HistorialEstadosModel> historial = orden.getHistorialEstados() != null
                ? new ArrayList<>(orden.getHistorialEstados())
                : new ArrayList<>();

        if (ordenAnterior == null) {
            if (historial.isEmpty()) {
                historial.add(new HistorialEstadosModel(
                        orden.getEstado(),
                        new Date(),
                        orden.getCoordinadorId(),
                        "Creación de orden de servicio"
                ));
            }
        } else if (!ordenAnterior.getEstado().equals(orden.getEstado())) {
            boolean yaRegistrado = !historial.isEmpty()
                    && orden.getEstado().equals(historial.get(historial.size() - 1).getEstado())
                    && historial.size() > (ordenAnterior.getHistorialEstados() != null ? ordenAnterior.getHistorialEstados().size() : 0);
            if (!yaRegistrado) {
                historial.add(new HistorialEstadosModel(
                        orden.getEstado(),
                        new Date(),
                        orden.getCoordinadorId(),
                        "Cambio de estado de " + ordenAnterior.getEstado() + " a " + orden.getEstado()
                ));
            }
        }
        orden.setHistorialEstados(historial);
    }

    @Override
    public OrdenesServiciosModel crearOrdenServicio(OrdenesServiciosModel ordenServicio) {
        validarOrden(ordenServicio, null);
        actualizarHistorialEstados(ordenServicio, null);
        OrdenesServiciosModel guardada = ordenesServiciosRepository.save(ordenServicio);

        if (ordenServicio.getCitaId() != null) {
            CitasModel cita = citasService.buscarCitaPorId(ordenServicio.getCitaId());
            if (cita != null) {
                cita.setEstado("atendida");
                citasService.actualizarCita(cita.getId(), cita);
            }
        }
        return guardada;
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
        OrdenesServiciosModel ordenAnterior = buscarOrdenServicioPorId(id);
        if (ordenAnterior == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + id + " no existe.");
        }
        validarOrden(ordenServicio, ordenAnterior);
        actualizarHistorialEstados(ordenServicio, ordenAnterior);
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

    @Override
    public Map<String, Object> calcularValorEstimadoPropuesta(ObjectId ordenId) {
        if (buscarOrdenServicioPorId(ordenId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + ordenId + " no existe.");
        }
        List<TrabajosOrdenModel> trabajos = trabajosOrdenRepository.findByOrdenId(ordenId);
        int totalManoObra = 0;
        int totalRepuestos = 0;

        for (TrabajosOrdenModel trabajo : trabajos) {
            if (trabajo.getManoObraEstimada() != null) {
                totalManoObra += trabajo.getManoObraEstimada();
            }
            if (trabajo.getRepuestosPrevistos() != null) {
                for (RepuestosPrevistosModel rep : trabajo.getRepuestosPrevistos()) {
                    if (rep.getCantidad() != null && rep.getPrecioUnitarioEstimado() != null) {
                        totalRepuestos += rep.getCantidad() * rep.getPrecioUnitarioEstimado();
                    }
                }
            }
        }

        Map<String, Object> resumen = new LinkedHashMap<>();
        resumen.put("ordenId", ordenId.toHexString());
        resumen.put("cantidadTrabajos", trabajos.size());
        resumen.put("totalManoObraEstimada", totalManoObra);
        resumen.put("totalRepuestosPrevistos", totalRepuestos);
        resumen.put("valorTotalEstimadoPropuesta", totalManoObra + totalRepuestos);
        return resumen;
    }
}