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

import backend.model.TrabajosOrdenModel;
import backend.service.ITrabajosOrdenService;

@RestController
@RequestMapping("/UAO/apirest/trabajosOrden")
public class TrabajosOrdenController {

    @Autowired
    ITrabajosOrdenService trabajosOrdenService;

    @PostMapping("/")
    public ResponseEntity<TrabajosOrdenModel> crearTrabajoOrden(@RequestBody TrabajosOrdenModel trabajoOrden) {
        return new ResponseEntity<TrabajosOrdenModel>(trabajosOrdenService.crearTrabajoOrden(trabajoOrden), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<TrabajosOrdenModel>> listarTrabajosOrden() {
        return new ResponseEntity<List<TrabajosOrdenModel>>(trabajosOrdenService.listarTrabajosOrden(), HttpStatus.OK);
    }
}