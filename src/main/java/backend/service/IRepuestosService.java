package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.RepuestosModel;

public interface IRepuestosService {
    RepuestosModel crearRepuesto(RepuestosModel repuesto);
    List<RepuestosModel> listarRepuestos();
    RepuestosModel buscarRepuestoPorId(ObjectId id);
}