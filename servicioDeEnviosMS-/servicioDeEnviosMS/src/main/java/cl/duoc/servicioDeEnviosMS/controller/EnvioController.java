package cl.duoc.servicioDeEnviosMS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.servicioDeEnviosMS.model.Envio;
import cl.duoc.servicioDeEnviosMS.service.EnvioService;

@RestController
@RequestMapping("/api/v1/Envios")
public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> listaEnvios() {
        var envios = service.listaEnvios();
        if (envios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(envios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> buscarPorOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(service.buscarPorOrderId(orderId));
    }

    @PostMapping
    public ResponseEntity<Envio> guardar(@RequestBody Envio envio) {
        try {
            Envio saved = service.guardarEnvio(envio);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
