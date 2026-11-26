package backend.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import backend.model.BicicletasModel;
import backend.model.CaracteristicasTecnicas;
import backend.model.UsuariosModel;
import backend.repository.IBicicletasRepository;

@Service
public class IBicicletasServiceImp implements IBicicletasService {

    private static final List<String> TIPOS_VALIDOS = List.of("urbana", "ruta", "montaña", "electrica", "eléctrica");

    @Autowired
    IBicicletasRepository bicicletasRepository;

    @Autowired
    IUsuariosService usuariosService;

    private void validarBicicleta(BicicletasModel bicicleta, ObjectId idActual) {
        if (bicicleta.getMarca() == null || bicicleta.getMarca().isBlank()
                || bicicleta.getModelo() == null || bicicleta.getModelo().isBlank()
                || bicicleta.getColor() == null || bicicleta.getColor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los campos marca, modelo y color son obligatorios.");
        }
        if (bicicleta.getTipo() == null || !TIPOS_VALIDOS.contains(bicicleta.getTipo().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de bicicleta debe ser uno de: " + TIPOS_VALIDOS);
        }

        CaracteristicasTecnicas ct = bicicleta.getCaracteristicasTecnicas();
        if (ct == null || ct.getTamanoMarco() == null || ct.getMaterial() == null
                || ct.getTamanoRueda() == null || ct.getTipoFrenos() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "caracteristicasTecnicas (tamanoMarco, material, tamanoRueda, tipoFrenos) es obligatorio.");
        }
        if (ct.getSuspension() != null && (ct.getSuspension().getInformacion() == null || ct.getSuspension().getRecorrido() == null || ct.getSuspension().getRecorrido() < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "suspension requiere informacion y recorrido >= 0.");
        }
        if (ct.getBateria() != null && (ct.getBateria().getCapacidadWh() == null || ct.getBateria().getCapacidadWh() < 0
                || ct.getBateria().getVoltajeV() == null || ct.getBateria().getVoltajeV() < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bateria requiere capacidadWh >= 0 y voltajeV >= 0.");
        }
        if (ct.getMotor() != null && (ct.getMotor().getTipo() == null || ct.getMotor().getPotenciaW() == null || ct.getMotor().getPotenciaW() < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "motor requiere tipo y potenciaW >= 0.");
        }

        ObjectId propietarioId = bicicleta.getPropietarioId();
        if (propietarioId != null) {
            UsuariosModel propietario = usuariosService.buscarUsuarioPorId(propietarioId);
            if (propietario == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario propietario con ID " + propietarioId + " no existe.");
            }
            if (!"cliente".equals(propietario.getRol())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El propietario de una bicicleta debe tener rol 'cliente'.");
            }
        }

        if (bicicleta.getNumeroSerie() != null && !bicicleta.getNumeroSerie().isBlank()) {
            Optional<BicicletasModel> existente = bicicletasRepository.findByNumeroSerie(bicicleta.getNumeroSerie());
            if (existente.isPresent() && !existente.get().getId().equals(idActual)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una bicicleta con el número de serie " + bicicleta.getNumeroSerie());
            }
        }
    }

    @Override
    public BicicletasModel crearBicicleta(BicicletasModel bicicleta) {
        validarBicicleta(bicicleta, null);
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
        validarBicicleta(bicicleta, id);
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