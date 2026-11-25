package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}