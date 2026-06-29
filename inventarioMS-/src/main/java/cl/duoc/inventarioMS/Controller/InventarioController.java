package cl.duoc.inventarioMS.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.inventarioMS.Model.Inventario;
import cl.duoc.inventarioMS.Service.InventarioService;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private InventarioService inventarioService;
    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<Inventario> listar() {
        return inventarioService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Inventario inventario) {
        Inventario nuevoInventario = inventarioService.guardar(inventario);
        if (nuevoInventario != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo registrar el inventario. Verifique el productoId.");
        }
    }
}