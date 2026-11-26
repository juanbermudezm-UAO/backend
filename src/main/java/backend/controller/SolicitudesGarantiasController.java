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

import backend.model.SolicitudesGarantiasModel;
import backend.service.ISolicitudesGarantiasService;

@RestController
@RequestMapping("/UAO/apirest/solicitudesGarantias")
public class SolicitudesGarantiasController {

    @Autowired
    ISolicitudesGarantiasService solicitudesGarantiasService;

    @PostMapping
    public ResponseEntity<SolicitudesGarantiasModel> crearSolicitudGarantia(@RequestBody SolicitudesGarantiasModel solicitudGarantia) {
        return new ResponseEntity<SolicitudesGarantiasModel>(solicitudesGarantiasService.crearSolicitudGarantia(solicitudGarantia), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudesGarantiasModel>> listarSolicitudesGarantias() {
        return new ResponseEntity<List<SolicitudesGarantiasModel>>(solicitudesGarantiasService.listarSolicitudesGarantias(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudesGarantiasModel> buscarSolicitudGarantiaPorId(@PathVariable ObjectId id) {
        SolicitudesGarantiasModel solicitud = solicitudesGarantiasService.buscarSolicitudGarantiaPorId(id);
        if (solicitud == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La solicitud de garantía con ID " + id + " no existe.");
        }
        return new ResponseEntity<SolicitudesGarantiasModel>(solicitud, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudesGarantiasModel> actualizarSolicitudGarantia(@PathVariable ObjectId id, @RequestBody SolicitudesGarantiasModel solicitudGarantia) {
        return new ResponseEntity<SolicitudesGarantiasModel>(solicitudesGarantiasService.actualizarSolicitudGarantia(id, solicitudGarantia), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarSolicitudGarantia(@PathVariable ObjectId id) {
        solicitudesGarantiasService.eliminarSolicitudGarantia(id);
        return new ResponseEntity<String>("Solicitud de garantía eliminada correctamente.", HttpStatus.OK);
    }
}