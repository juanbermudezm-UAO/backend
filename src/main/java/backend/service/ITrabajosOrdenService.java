package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.TrabajosOrdenModel;

public interface ITrabajosOrdenService {
    TrabajosOrdenModel crearTrabajoOrden(TrabajosOrdenModel trabajoOrden);
    List<TrabajosOrdenModel> listarTrabajosOrden();
    TrabajosOrdenModel buscarTrabajoOrdenPorId(ObjectId id);
    TrabajosOrdenModel actualizarTrabajoOrden(ObjectId id, TrabajosOrdenModel trabajoOrden);
    void eliminarTrabajoOrden(ObjectId id);
}