package backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.CitasModel;

public interface ICitasRepository extends MongoRepository<CitasModel, ObjectId> {
    // consultas nativas (agregaciones)
}