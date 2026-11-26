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

import backend.model.ServiciosModel;
import backend.service.IServiciosService;

@RestController
@RequestMapping("/UAO/apirest/servicios")
public class ServiciosController {

    @Autowired
    IServiciosService serviciosService;

    @PostMapping
    public ResponseEntity<ServiciosModel> crearServicio(@RequestBody ServiciosModel servicio) {
        return new ResponseEntity<ServiciosModel>(serviciosService.crearServicio(servicio), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServiciosModel>> listarServicios() {
        return new ResponseEntity<List<ServiciosModel>>(serviciosService.listarServicios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiciosModel> buscarServicioPorId(@PathVariable ObjectId id) {
        ServiciosModel servicio = serviciosService.buscarServicioPorId(id);
        if (servicio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio con ID " + id + " no existe.");
        }
        return new ResponseEntity<ServiciosModel>(servicio, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiciosModel> actualizarServicio(@PathVariable ObjectId id, @RequestBody ServiciosModel servicio) {
        return new ResponseEntity<ServiciosModel>(serviciosService.actualizarServicio(id, servicio), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarServicio(@PathVariable ObjectId id) {
        serviciosService.eliminarServicio(id);
        return new ResponseEntity<String>("Servicio eliminado correctamente.", HttpStatus.OK);
    }
}