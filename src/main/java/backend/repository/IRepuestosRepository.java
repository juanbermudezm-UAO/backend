package backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.RepuestosModel;

public interface IRepuestosRepository extends MongoRepository<RepuestosModel, ObjectId> {
    // consultas nativas (agregaciones)
}