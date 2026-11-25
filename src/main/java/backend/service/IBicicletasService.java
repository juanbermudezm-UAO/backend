package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.BicicletasModel;

public interface IBicicletasService {
    BicicletasModel crearBicicleta(BicicletasModel bicicleta);
    List<BicicletasModel> listarBicicletas();
    BicicletasModel buscarBicicletaPorId(ObjectId id);
}