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

import backend.model.UsuariosModel;
import backend.service.IUsuariosService;

@RestController
@RequestMapping("/UAO/apirest/usuarios")
public class UsuariosController {

    @Autowired
    IUsuariosService usuariosService;

    @PostMapping("/")
    public ResponseEntity<UsuariosModel> crearUsuario(@RequestBody UsuariosModel usuario) {
        return new ResponseEntity<UsuariosModel>(usuariosService.crearUsuario(usuario), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<UsuariosModel>> listarUsuarios() {
        return new ResponseEntity<List<UsuariosModel>>(usuariosService.listarUsuarios(), HttpStatus.OK);
    }
}