package backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.TrabajosOrdenModel;

public interface ITrabajosOrdenRepository extends MongoRepository<TrabajosOrdenModel, ObjectId> {
    // consultas nativas (agregaciones)
}