package backend.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.OrdenesServiciosModel;

public interface IOrdenesServiciosRepository extends MongoRepository<OrdenesServiciosModel, ObjectId> {
    List<OrdenesServiciosModel> findByBicicleta_BicicletaIdAndEstadoNotIn(ObjectId bicicletaId, List<String> estadosFinales);
}