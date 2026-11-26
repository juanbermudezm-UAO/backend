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

import backend.model.CitasModel;
import backend.service.ICitasService;

@RestController
@RequestMapping("/UAO/apirest/citas")
public class CitasController {

    @Autowired
    ICitasService citasService;

    @PostMapping
    public ResponseEntity<CitasModel> crearCita(@RequestBody CitasModel cita) {
        return new ResponseEntity<CitasModel>(citasService.crearCita(cita), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CitasModel>> listarCitas() {
        return new ResponseEntity<List<CitasModel>>(citasService.listarCitas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitasModel> buscarCitaPorId(@PathVariable ObjectId id) {
        CitasModel cita = citasService.buscarCitaPorId(id);
        if (cita == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cita con ID " + id + " no existe.");
        }
        return new ResponseEntity<CitasModel>(cita, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitasModel> actualizarCita(@PathVariable ObjectId id, @RequestBody CitasModel cita) {
        return new ResponseEntity<CitasModel>(citasService.actualizarCita(id, cita), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCita(@PathVariable ObjectId id) {
        citasService.eliminarCita(id);
        return new ResponseEntity<String>("Cita eliminada correctamente.", HttpStatus.OK);
    }
}