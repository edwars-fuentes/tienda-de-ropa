package cl.duoc.PromocionesMS.Controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.PromocionesMS.Model.Promocion;
import cl.duoc.PromocionesMS.Service.PromocionService;

@RestController
@RequestMapping("/api/v1/promociones")
public class PromocionController {

    private final PromocionService service;
    public PromocionController(PromocionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Promocion>> listar() {
        List<Promocion> lista = service.listarTodas();
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<Promocion> crear(@RequestBody Promocion promocion) {
        Promocion saved = service.guardar(promocion);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }
}