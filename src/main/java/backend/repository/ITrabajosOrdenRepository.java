package backend.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.TrabajosOrdenModel;

public interface ITrabajosOrdenRepository extends MongoRepository<TrabajosOrdenModel, ObjectId> {
    List<TrabajosOrdenModel> findByOrdenId(ObjectId ordenId);
}