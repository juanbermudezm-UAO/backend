package backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.model.SolicitudesGarantiasModel;
import backend.service.ISolicitudesGarantiasService;

@RestController
@RequestMapping("/UAO/apirest/solicitudesGarantias")
public class SolicitudesGarantiasController {

    @Autowired
    ISolicitudesGarantiasService solicitudesGarantiasService;

    @PostMapping("/")
    public ResponseEntity<SolicitudesGarantiasModel> crearSolicitudGarantia(@RequestBody SolicitudesGarantiasModel solicitudGarantia) {
        return new ResponseEntity<SolicitudesGarantiasModel>(solicitudesGarantiasService.crearSolicitudGarantia(solicitudGarantia), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SolicitudesGarantiasModel>> listarSolicitudesGarantias() {
        return new ResponseEntity<List<SolicitudesGarantiasModel>>(solicitudesGarantiasService.listarSolicitudesGarantias(), HttpStatus.OK);
    }
}