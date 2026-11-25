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

import backend.model.CitasModel;
import backend.service.ICitasService;

@RestController
@RequestMapping("/UAO/apirest/citas")
public class CitasController {

    @Autowired
    ICitasService citasService;

    @PostMapping("/")
    public ResponseEntity<CitasModel> crearCita(@RequestBody CitasModel cita) {
        return new ResponseEntity<CitasModel>(citasService.crearCita(cita), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<CitasModel>> listarCitas() {
        return new ResponseEntity<List<CitasModel>>(citasService.listarCitas(), HttpStatus.OK);
    }
}