package cl.duoc.UsuariosMs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.UsuariosMs.model.Usuarios;
import cl.duoc.UsuariosMs.service.UsuariosService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    

    private final UsuariosService usuariosService;

    // Inyección por constructor (preferible para tests y claridad)
    public UsuarioController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @GetMapping
    public List<Usuarios> listar() {
        return usuariosService.listarTodos(); // usa usuariosService, no usuarioService
    }

    @PostMapping
    public ResponseEntity<Usuarios> crear(@RequestBody Usuarios usuario) {
        Usuarios creado = usuariosService.guardar(usuario);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> obtenerPorId(@PathVariable("id") Integer id) {
        Usuarios usuario = usuariosService.buscarPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}