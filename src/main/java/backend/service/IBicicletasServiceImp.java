package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.BicicletasModel;
import backend.model.UsuariosModel;
import backend.repository.IBicicletasRepository;

@Service
public class IBicicletasServiceImp implements IBicicletasService {

    @Autowired
    IBicicletasRepository bicicletasRepository;

    @Autowired
    IUsuariosService usuariosService;

    @Override
    public BicicletasModel crearBicicleta(BicicletasModel bicicleta) {
        ObjectId propietarioId = bicicleta.getPropietarioId();
        if (propietarioId != null) {
            UsuariosModel propietario = usuariosService.buscarUsuarioPorId(propietarioId);
            if (propietario == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario propietario con ID " + propietarioId + " no existe.");
            }
        }
        return bicicletasRepository.save(bicicleta);
    }

    @Override
    public List<BicicletasModel> listarBicicletas() {
        return bicicletasRepository.findAll();
    }

    @Override
    public BicicletasModel buscarBicicletaPorId(ObjectId id) {
        return bicicletasRepository.findById(id).orElse(null);
    }
}