package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.UsuariosModel;
import backend.repository.IUsuariosRepository;

@Service
public class IUsuariosServiceImp implements IUsuariosService {

    @Autowired
    IUsuariosRepository usuariosRepository;

    @Override
    public UsuariosModel crearUsuario(UsuariosModel usuario) {
        return usuariosRepository.save(usuario);
    }

    @Override
    public List<UsuariosModel> listarUsuarios() {
        return usuariosRepository.findAll();
    }

    @Override
    public UsuariosModel buscarUsuarioPorId(ObjectId id) {
        return usuariosRepository.findById(id).orElse(null);
    }

    @Override
    public UsuariosModel actualizarUsuario(ObjectId id, UsuariosModel usuario) {
        if (buscarUsuarioPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + id + " no existe.");
        }
        usuario.setId(id);
        return usuariosRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(ObjectId id) {
        if (buscarUsuarioPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + id + " no existe.");
        }
        usuariosRepository.deleteById(id);
    }
}