package backend.repository;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.CitasModel;

public interface ICitasRepository extends MongoRepository<CitasModel, ObjectId> {
    List<CitasModel> findByBicicletaIdAndFechaHoraAndEstadoIn(ObjectId bicicletaId, Date fechaHora, List<String> estados);
}