package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.ServiciosModel;
import backend.model.TareasHabitualesModel;
import backend.repository.IServiciosRepository;

@Service
public class IServiciosServiceImp implements IServiciosService {

    private static final List<String> CATEGORIAS_VALIDAS = List.of(
            "mantenimiento preventivo",
            "frenos",
            "transmision",
            "ruedas",
            "suspension",
            "componentes electricos"
    );

    @Autowired
    IServiciosRepository serviciosRepository;

    private void validarServicio(ServiciosModel servicio) {
        if (servicio.getNombre() == null || servicio.getNombre().isBlank()
                || servicio.getDescripcion() == null || servicio.getDescripcion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los campos nombre y descripcion son obligatorios.");
        }
        if (servicio.getCategoria() == null || !CATEGORIAS_VALIDAS.contains(servicio.getCategoria())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría debe ser una de: " + CATEGORIAS_VALIDAS);
        }
        if (servicio.getDuracionEstimadaMin() == null || servicio.getDuracionEstimadaMin() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duracionEstimadaMin debe ser >= 1.");
        }
        if (servicio.getPrecioReferencia() == null || servicio.getPrecioReferencia() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "precioReferencia debe ser >= 0.");
        }
        if (servicio.getTareasHabituales() != null) {
            for (TareasHabitualesModel tarea : servicio.getTareasHabituales()) {
                if (tarea.getNombre() == null || tarea.getNombre().isBlank()
                        || tarea.getDescripcion() == null || tarea.getDescripcion().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada tarea habitual requiere nombre y descripcion.");
                }
            }
        }
    }

    @Override
    public ServiciosModel crearServicio(ServiciosModel servicio) {
        validarServicio(servicio);
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
        validarServicio(servicio);
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