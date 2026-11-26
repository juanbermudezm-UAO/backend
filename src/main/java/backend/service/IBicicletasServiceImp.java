package backend.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.BicicletasModel;
import backend.repository.IBicicletasRepository;

@Service
public class IBicicletasServiceImp implements IBicicletasService {

    @Autowired
    IBicicletasRepository bicicletasRepository;

    @Autowired
    IUsuariosService usuariosService;

    private void validarReferencias(BicicletasModel bicicleta) {
        ObjectId propietarioId = bicicleta.getPropietarioId();
        if (propietarioId != null && usuariosService.buscarUsuarioPorId(propietarioId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario propietario con ID " + propietarioId + " no existe.");
        }
    }

    @Override
    public BicicletasModel crearBicicleta(BicicletasModel bicicleta) {
        validarReferencias(bicicleta);
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

    @Override
    public BicicletasModel actualizarBicicleta(ObjectId id, BicicletasModel bicicleta) {
        if (buscarBicicletaPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + id + " no existe.");
        }
        validarReferencias(bicicleta);
        bicicleta.setId(id);
        return bicicletasRepository.save(bicicleta);
    }

    @Override
    public void eliminarBicicleta(ObjectId id) {
        if (buscarBicicletaPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La bicicleta con ID " + id + " no existe.");
        }
        bicicletasRepository.deleteById(id);
    }
}