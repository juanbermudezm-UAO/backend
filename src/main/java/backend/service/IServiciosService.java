package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.ServiciosModel;

public interface IServiciosService {
    ServiciosModel crearServicio(ServiciosModel servicio);
    List<ServiciosModel> listarServicios();
    ServiciosModel buscarServicioPorId(ObjectId id);
    ServiciosModel actualizarServicio(ObjectId id, ServiciosModel servicio);
    void eliminarServicio(ObjectId id);
}