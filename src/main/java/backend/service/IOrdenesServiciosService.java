package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.OrdenesServiciosModel;

public interface IOrdenesServiciosService {
    OrdenesServiciosModel crearOrdenServicio(OrdenesServiciosModel ordenServicio);
    List<OrdenesServiciosModel> listarOrdenesServicios();
    OrdenesServiciosModel buscarOrdenServicioPorId(ObjectId id);
}