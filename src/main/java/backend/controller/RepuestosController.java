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

import backend.model.RepuestosModel;
import backend.service.IRepuestosService;

@RestController
@RequestMapping("/UAO/apirest/repuestos")
public class RepuestosController {

    @Autowired
    IRepuestosService repuestosService;

    @PostMapping("/")
    public ResponseEntity<RepuestosModel> crearRepuesto(@RequestBody RepuestosModel repuesto) {
        return new ResponseEntity<RepuestosModel>(repuestosService.crearRepuesto(repuesto), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<RepuestosModel>> listarRepuestos() {
        return new ResponseEntity<List<RepuestosModel>>(repuestosService.listarRepuestos(), HttpStatus.OK);
    }
}