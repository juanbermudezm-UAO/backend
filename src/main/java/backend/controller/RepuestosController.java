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

import backend.model.RepuestosModel;
import backend.service.IRepuestosService;

@RestController
@RequestMapping("/UAO/apirest/repuestos")
public class RepuestosController {

    @Autowired
    IRepuestosService repuestosService;

    @PostMapping
    public ResponseEntity<RepuestosModel> crearRepuesto(@RequestBody RepuestosModel repuesto) {
        return new ResponseEntity<RepuestosModel>(repuestosService.crearRepuesto(repuesto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RepuestosModel>> listarRepuestos() {
        return new ResponseEntity<List<RepuestosModel>>(repuestosService.listarRepuestos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepuestosModel> buscarRepuestoPorId(@PathVariable ObjectId id) {
        RepuestosModel repuesto = repuestosService.buscarRepuestoPorId(id);
        if (repuesto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El repuesto con ID " + id + " no existe.");
        }
        return new ResponseEntity<RepuestosModel>(repuesto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepuestosModel> actualizarRepuesto(@PathVariable ObjectId id, @RequestBody RepuestosModel repuesto) {
        return new ResponseEntity<RepuestosModel>(repuestosService.actualizarRepuesto(id, repuesto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRepuesto(@PathVariable ObjectId id) {
        repuestosService.eliminarRepuesto(id);
        return new ResponseEntity<String>("Repuesto eliminado correctamente.", HttpStatus.OK);
    }
}