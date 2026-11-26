package backend.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import backend.model.OrdenesServiciosModel;

public interface IOrdenesServiciosService {
    OrdenesServiciosModel crearOrdenServicio(OrdenesServiciosModel ordenServicio);
    List<OrdenesServiciosModel> listarOrdenesServicios();
    OrdenesServiciosModel buscarOrdenServicioPorId(ObjectId id);
    OrdenesServiciosModel actualizarOrdenServicio(ObjectId id, OrdenesServiciosModel ordenServicio);
    void eliminarOrdenServicio(ObjectId id);
    Map<String, Object> calcularValorEstimadoPropuesta(ObjectId ordenId);
}