package backend.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import backend.model.OrdenesServiciosModel;
import backend.service.IOrdenesServiciosService;

@RestController
@RequestMapping("/UAO/apirest/ordenesServicios")
public class OrdenesServiciosController {

    @Autowired
    IOrdenesServiciosService ordenesServiciosService;

    @PostMapping
    public ResponseEntity<OrdenesServiciosModel> crearOrdenServicio(@RequestBody OrdenesServiciosModel ordenServicio) {
        return new ResponseEntity<OrdenesServiciosModel>(ordenesServiciosService.crearOrdenServicio(ordenServicio),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrdenesServiciosModel>> listarOrdenesServicios() {
        return new ResponseEntity<List<OrdenesServiciosModel>>(ordenesServiciosService.listarOrdenesServicios(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenesServiciosModel> buscarOrdenServicioPorId(@PathVariable ObjectId id) {
        OrdenesServiciosModel orden = ordenesServiciosService.buscarOrdenServicioPorId(id);
        if (orden == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "La orden de servicio con ID " + id + " no existe.");
        }
        return new ResponseEntity<OrdenesServiciosModel>(orden, HttpStatus.OK);
    }

    @GetMapping("/{id}/valorEstimado")
    public ResponseEntity<Object> calcularValorEstimadoPropuesta(@PathVariable ObjectId id) {
        return new ResponseEntity<>(ordenesServiciosService.calcularValorEstimadoPropuesta(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenesServiciosModel> actualizarOrdenServicio(@PathVariable ObjectId id,
            @RequestBody OrdenesServiciosModel ordenServicio) {
        return new ResponseEntity<OrdenesServiciosModel>(
                ordenesServiciosService.actualizarOrdenServicio(id, ordenServicio), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarOrdenServicio(@PathVariable ObjectId id) {
        ordenesServiciosService.eliminarOrdenServicio(id);
        return new ResponseEntity<String>("Orden de servicio eliminada correctamente.", HttpStatus.OK);
    }
}