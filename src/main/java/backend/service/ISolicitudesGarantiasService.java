package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.SolicitudesGarantiasModel;

public interface ISolicitudesGarantiasService {
    SolicitudesGarantiasModel crearSolicitudGarantia(SolicitudesGarantiasModel solicitudGarantia);
    List<SolicitudesGarantiasModel> listarSolicitudesGarantias();
    SolicitudesGarantiasModel buscarSolicitudGarantiaPorId(ObjectId id);
}