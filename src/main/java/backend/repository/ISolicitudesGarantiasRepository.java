package backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.SolicitudesGarantiasModel;

public interface ISolicitudesGarantiasRepository extends MongoRepository<SolicitudesGarantiasModel, ObjectId> {
    // consultas nativas (agregaciones)
}