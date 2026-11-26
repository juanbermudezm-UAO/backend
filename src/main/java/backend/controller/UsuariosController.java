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

import backend.model.UsuariosModel;
import backend.service.IUsuariosService;

@RestController
@RequestMapping("/UAO/apirest/usuarios")
public class UsuariosController {

    @Autowired
    IUsuariosService usuariosService;

    @PostMapping
    public ResponseEntity<UsuariosModel> crearUsuario(@RequestBody UsuariosModel usuario) {
        return new ResponseEntity<UsuariosModel>(usuariosService.crearUsuario(usuario), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuariosModel>> listarUsuarios() {
        return new ResponseEntity<List<UsuariosModel>>(usuariosService.listarUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuariosModel> buscarUsuarioPorId(@PathVariable ObjectId id) {
        UsuariosModel usuario = usuariosService.buscarUsuarioPorId(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con ID " + id + " no existe.");
        }
        return new ResponseEntity<UsuariosModel>(usuario, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuariosModel> actualizarUsuario(@PathVariable ObjectId id, @RequestBody UsuariosModel usuario) {
        return new ResponseEntity<UsuariosModel>(usuariosService.actualizarUsuario(id, usuario), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable ObjectId id) {
        usuariosService.eliminarUsuario(id);
        return new ResponseEntity<String>("Usuario eliminado correctamente.", HttpStatus.OK);
    }
}