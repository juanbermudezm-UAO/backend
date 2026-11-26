package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.CitasModel;

public interface ICitasService {
    CitasModel crearCita(CitasModel cita);
    List<CitasModel> listarCitas();
    CitasModel buscarCitaPorId(ObjectId id);
    CitasModel actualizarCita(ObjectId id, CitasModel cita);
    void eliminarCita(ObjectId id);
}