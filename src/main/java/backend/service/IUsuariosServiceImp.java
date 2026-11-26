package backend.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.UsuariosModel;
import backend.repository.IUsuariosRepository;

@Service
public class IUsuariosServiceImp implements IUsuariosService {

    private static final List<String> ROLES_VALIDOS = List.of("cliente", "tecnico", "coordinador");

    @Autowired
    IUsuariosRepository usuariosRepository;

    private void validarUsuario(UsuariosModel usuario, ObjectId idActual) {
        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo nombreCompleto es obligatorio.");
        }
        if (usuario.getDocumentoIdentificacion() == null || usuario.getDocumentoIdentificacion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo documentoIdentificacion es obligatorio.");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo correo es obligatorio.");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().length() < 7 || usuario.getTelefono().length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El teléfono es obligatorio y debe tener entre 7 y 20 caracteres.");
        }
        if (usuario.getRol() == null || !ROLES_VALIDOS.contains(usuario.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol debe ser uno de: " + ROLES_VALIDOS);
        }

        Optional<UsuariosModel> porDocumento = usuariosRepository.findByDocumentoIdentificacion(usuario.getDocumentoIdentificacion());
        if (porDocumento.isPresent() && !porDocumento.get().getId().equals(idActual)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con el documento " + usuario.getDocumentoIdentificacion());
        }

        Optional<UsuariosModel> porCorreo = usuariosRepository.findByCorreo(usuario.getCorreo());
        if (porCorreo.isPresent() && !porCorreo.get().getId().equals(idActual)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con el correo " + usuario.getCorreo());
        }
    }

    @Override
    public UsuariosModel crearUsuario(UsuariosModel usuario) {
        validarUsuario(usuario, null);
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
        validarUsuario(usuario, id);
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