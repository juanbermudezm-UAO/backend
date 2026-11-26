package backend.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.BicicletasModel;

public interface IBicicletasRepository extends MongoRepository<BicicletasModel, ObjectId> {
    Optional<BicicletasModel> findByNumeroSerie(String numeroSerie);
}