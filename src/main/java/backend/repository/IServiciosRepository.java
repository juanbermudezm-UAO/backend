package backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.ServiciosModel;

public interface IServiciosRepository extends MongoRepository<ServiciosModel, ObjectId> {
    // consultas nativas (agregaciones)
}