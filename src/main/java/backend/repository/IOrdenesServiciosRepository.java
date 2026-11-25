package backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.OrdenesServiciosModel;

public interface IOrdenesServiciosRepository extends MongoRepository<OrdenesServiciosModel, ObjectId> {
    // consultas nativas (agregaciones)
}