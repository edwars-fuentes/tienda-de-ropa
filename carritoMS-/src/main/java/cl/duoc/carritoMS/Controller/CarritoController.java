package cl.duoc.carritoMS.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.duoc.carritoMS.Model.Carrito;
import cl.duoc.carritoMS.Service.CarritoService;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    private final CarritoService service;
    public CarritoController(CarritoService service) {
    this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Carrito carrito) {
        Carrito carritoGuardado = service.guardar(carrito);
        
        if (carritoGuardado != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(carritoGuardado);
        } else {
            // Si el servicio devolvió null (porque falló alguna validación)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: No se pudo crear el carrito. Verifica el usuario o producto.");
        }
    }
}
