package backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.BicicletasModel;

public interface IBicicletasRepository extends MongoRepository<BicicletasModel, ObjectId> {
    // consultas nativas (agregaciones)
}