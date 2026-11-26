package backend.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.OrdenesServiciosModel;
import backend.model.RepuestosPrevistosModel;
import backend.model.RepuestosUtilizadosModel;
import backend.model.TrabajosOrdenModel;
import backend.model.UsuariosModel;
import backend.repository.ITrabajosOrdenRepository;

@Service
public class ITrabajosOrdenServiceImp implements ITrabajosOrdenService {

    private static final List<String> DECISIONES_VALIDAS = List.of("autorizado", "rechazado");
    private static final List<String> ESTADOS_EJECUCION_VALIDOS = List.of("pendiente", "en_progreso", "finalizado", "cancelado");

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

    private void validarTrabajoOrden(TrabajosOrdenModel trabajo) {
        if (trabajo.getOrdenId() == null || trabajo.getTecnicoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los campos ordenId y tecnicoId son obligatorios.");
        }

        OrdenesServiciosModel orden = ordenesServiciosService.buscarOrdenServicioPorId(trabajo.getOrdenId());
        if (orden == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La orden de servicio con ID " + trabajo.getOrdenId() + " no existe.");
        }
        if ("entregada".equals(orden.getEstado()) || "cancelada".equals(orden.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden agregar o modificar trabajos en una orden entregada o cancelada.");
        }

        UsuariosModel tecnico = usuariosService.buscarUsuarioPorId(trabajo.getTecnicoId());
        if (tecnico == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El técnico con ID " + trabajo.getTecnicoId() + " no existe.");
        }
        if (!"tecnico".equals(tecnico.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario tecnicoId debe tener rol 'tecnico'.");
        }

        if (trabajo.getServicio() != null) {
            ObjectId servicioId = trabajo.getServicio().getServicioId();
            if (serviciosService.buscarServicioPorId(servicioId) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + servicioId + " no existe.");
            }
        }

        if (trabajo.getManoObraEstimada() != null && trabajo.getManoObraEstimada() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "manoObraEstimada debe ser >= 0.");
        }

        if (trabajo.getRepuestosPrevistos() != null) {
            for (RepuestosPrevistosModel rep : trabajo.getRepuestosPrevistos()) {
                if (repuestosService.buscarRepuestoPorId(rep.getRepuestoId()) == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto previsto con ID " + rep.getRepuestoId() + " no existe.");
                }
                if (rep.getCantidad() == null || rep.getCantidad() < 1 || rep.getPrecioUnitarioEstimado() == null || rep.getPrecioUnitarioEstimado() < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada repuesto previsto requiere cantidad >= 1 y precioUnitarioEstimado >= 0.");
                }
            }
        }

        if (trabajo.getAutorizacion() != null) {
            String decision = trabajo.getAutorizacion().getDecision();
            if (decision == null || !DECISIONES_VALIDAS.contains(decision.toLowerCase())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La decisión de autorización debe ser 'autorizado' o 'rechazado'.");
            }
        }

        // Regla: Solo los trabajos autorizados pueden pasar a ejecución
        if (trabajo.getEjecucion() != null) {
            if (trabajo.getAutorizacion() == null || !"autorizado".equalsIgnoreCase(trabajo.getAutorizacion().getDecision())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo los trabajos con autorización 'autorizado' pueden ejecutarse.");
            }
            String estadoEj = trabajo.getEjecucion().getEstado();
            if (estadoEj == null || !ESTADOS_EJECUCION_VALIDOS.contains(estadoEj)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado de ejecución debe ser uno de: " + ESTADOS_EJECUCION_VALIDOS);
            }
            if (trabajo.getEjecucion().getRepuestosUtilizados() != null) {
                for (RepuestosUtilizadosModel rep : trabajo.getEjecucion().getRepuestosUtilizados()) {
                    if (repuestosService.buscarRepuestoPorId(rep.getRepuestoId()) == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto utilizado con ID " + rep.getRepuestoId() + " no existe.");
                    }
                    if (rep.getCantidad() == null || rep.getCantidad() < 1 || rep.getPrecioUnitarioAplicado() == null || rep.getPrecioUnitarioAplicado() < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada repuesto utilizado requiere cantidad >= 1 y precioUnitarioAplicado >= 0.");
                    }
                }
            }
        }

        if (trabajo.getRevision() != null) {
            if (trabajo.getEjecucion() == null || !"finalizado".equalsIgnoreCase(trabajo.getEjecucion().getEstado())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede registrar revisión hasta que la ejecución del trabajo esté en estado 'finalizado'.");
            }
        }
    }

    @Override
    public TrabajosOrdenModel crearTrabajoOrden(TrabajosOrdenModel trabajoOrden) {
        validarTrabajoOrden(trabajoOrden);
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

    @Override
    public TrabajosOrdenModel actualizarTrabajoOrden(ObjectId id, TrabajosOrdenModel trabajoOrden) {
        if (buscarTrabajoOrdenPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo de orden con ID " + id + " no existe.");
        }
        validarTrabajoOrden(trabajoOrden);
        trabajoOrden.setId(id);
        return trabajosOrdenRepository.save(trabajoOrden);
    }

    @Override
    public void eliminarTrabajoOrden(ObjectId id) {
        if (buscarTrabajoOrdenPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo de orden con ID " + id + " no existe.");
        }
        trabajosOrdenRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> calcularValorEstimadoTrabajo(ObjectId trabajoId) {
        TrabajosOrdenModel trabajo = buscarTrabajoOrdenPorId(trabajoId);
        if (trabajo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo de orden con ID " + trabajoId + " no existe.");
        }
        int manoObra = trabajo.getManoObraEstimada() != null ? trabajo.getManoObraEstimada() : 0;
        int subtotalRepuestos = 0;
        if (trabajo.getRepuestosPrevistos() != null) {
            for (RepuestosPrevistosModel rep : trabajo.getRepuestosPrevistos()) {
                if (rep.getCantidad() != null && rep.getPrecioUnitarioEstimado() != null) {
                    subtotalRepuestos += rep.getCantidad() * rep.getPrecioUnitarioEstimado();
                }
            }
        }

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("trabajoId", trabajoId.toHexString());
        resultado.put("manoObraEstimada", manoObra);
        resultado.put("subtotalRepuestosPrevistos", subtotalRepuestos);
        resultado.put("valorEstimadoTrabajo", manoObra + subtotalRepuestos);
        return resultado;
    }
}