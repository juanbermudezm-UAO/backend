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

import backend.model.ServiciosModel;
import backend.service.IServiciosService;

@RestController
@RequestMapping("/UAO/apirest/servicios")
public class ServiciosController {

    @Autowired
    IServiciosService serviciosService;

    @PostMapping("/")
    public ResponseEntity<ServiciosModel> crearServicio(@RequestBody ServiciosModel servicio) {
        return new ResponseEntity<ServiciosModel>(serviciosService.crearServicio(servicio), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ServiciosModel>> listarServicios() {
        return new ResponseEntity<List<ServiciosModel>>(serviciosService.listarServicios(), HttpStatus.OK);
    }
}