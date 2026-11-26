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

import backend.model.TrabajosOrdenModel;
import backend.service.ITrabajosOrdenService;

@RestController
@RequestMapping("/UAO/apirest/trabajosOrden")
public class TrabajosOrdenController {

    @Autowired
    ITrabajosOrdenService trabajosOrdenService;

    @PostMapping
    public ResponseEntity<TrabajosOrdenModel> crearTrabajoOrden(@RequestBody TrabajosOrdenModel trabajoOrden) {
        return new ResponseEntity<TrabajosOrdenModel>(trabajosOrdenService.crearTrabajoOrden(trabajoOrden),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrabajosOrdenModel>> listarTrabajosOrden() {
        return new ResponseEntity<List<TrabajosOrdenModel>>(trabajosOrdenService.listarTrabajosOrden(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrabajosOrdenModel> buscarTrabajoOrdenPorId(@PathVariable ObjectId id) {
        TrabajosOrdenModel trabajo = trabajosOrdenService.buscarTrabajoOrdenPorId(id);
        if (trabajo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El trabajo de orden con ID " + id + " no existe.");
        }
        return new ResponseEntity<TrabajosOrdenModel>(trabajo, HttpStatus.OK);
    }

    @GetMapping("/{id}/valorEstimado")
    public ResponseEntity<Object> calcularValorEstimadoTrabajo(@PathVariable ObjectId id) {
        return new ResponseEntity<>(trabajosOrdenService.calcularValorEstimadoTrabajo(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrabajosOrdenModel> actualizarTrabajoOrden(@PathVariable ObjectId id,
            @RequestBody TrabajosOrdenModel trabajoOrden) {
        return new ResponseEntity<TrabajosOrdenModel>(trabajosOrdenService.actualizarTrabajoOrden(id, trabajoOrden),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTrabajoOrden(@PathVariable ObjectId id) {
        trabajosOrdenService.eliminarTrabajoOrden(id);
        return new ResponseEntity<String>("Trabajo de orden eliminado correctamente.", HttpStatus.OK);
    }
}