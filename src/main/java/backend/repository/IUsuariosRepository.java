package backend.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import backend.model.UsuariosModel;

public interface IUsuariosRepository extends MongoRepository<UsuariosModel, ObjectId> {
    Optional<UsuariosModel> findByDocumentoIdentificacion(String documentoIdentificacion);
    Optional<UsuariosModel> findByCorreo(String correo);
}