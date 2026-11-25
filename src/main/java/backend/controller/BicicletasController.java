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

import backend.model.BicicletasModel;
import backend.service.IBicicletasService;

@RestController
@RequestMapping("/UAO/apirest/bicicletas")
public class BicicletasController {

    @Autowired
    IBicicletasService bicicletasService;

    @PostMapping("/")
    public ResponseEntity<BicicletasModel> crearBicicleta(@RequestBody BicicletasModel bicicleta) {
        return new ResponseEntity<BicicletasModel>(bicicletasService.crearBicicleta(bicicleta), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<BicicletasModel>> listarBicicletas() {
        return new ResponseEntity<List<BicicletasModel>>(bicicletasService.listarBicicletas(), HttpStatus.OK);
    }
}