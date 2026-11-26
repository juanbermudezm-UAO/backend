package backend.service;

import java.util.List;

import org.bson.types.ObjectId;

import backend.model.UsuariosModel;

public interface IUsuariosService {
    UsuariosModel crearUsuario(UsuariosModel usuario);
    List<UsuariosModel> listarUsuarios();
    UsuariosModel buscarUsuarioPorId(ObjectId id);
    UsuariosModel actualizarUsuario(ObjectId id, UsuariosModel usuario);
    void eliminarUsuario(ObjectId id);
}