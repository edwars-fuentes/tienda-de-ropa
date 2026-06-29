package cl.duoc.pedidosMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.pedidosMS.model.ProductoAEnviar;
import cl.duoc.pedidosMS.service.PedidosService;

@RestController
@RequestMapping("/api/v1/productos_enviados")
public class PedidoController {

    private final PedidosService service;

    public PedidoController(PedidosService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductoAEnviar>> listaPAE(){
        List<ProductoAEnviar> listaPAE = service.listaPAE();
        if (listaPAE == null || listaPAE.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaPAE);
    }

    @PostMapping
    public ResponseEntity<ProductoAEnviar> guardar(@RequestBody ProductoAEnviar PAE){
        try {
            ProductoAEnviar saved = service.guardar(PAE);
            // si quieres 201: return ResponseEntity.created(...).body(saved);
            return ResponseEntity.ok().build(); // coincide con tu test actual
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoAEnviar> buscarPorId(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}