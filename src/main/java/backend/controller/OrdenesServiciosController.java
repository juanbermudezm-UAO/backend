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

import backend.model.OrdenesServiciosModel;
import backend.service.IOrdenesServiciosService;

@RestController
@RequestMapping("/UAO/apirest/ordenesServicios")
public class OrdenesServiciosController {

    @Autowired
    IOrdenesServiciosService ordenesServiciosService;

    @PostMapping("/")
    public ResponseEntity<OrdenesServiciosModel> crearOrdenServicio(@RequestBody OrdenesServiciosModel ordenServicio) {
        return new ResponseEntity<OrdenesServiciosModel>(ordenesServiciosService.crearOrdenServicio(ordenServicio), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrdenesServiciosModel>> listarOrdenesServicios() {
        return new ResponseEntity<List<OrdenesServiciosModel>>(ordenesServiciosService.listarOrdenesServicios(), HttpStatus.OK);
    }
}