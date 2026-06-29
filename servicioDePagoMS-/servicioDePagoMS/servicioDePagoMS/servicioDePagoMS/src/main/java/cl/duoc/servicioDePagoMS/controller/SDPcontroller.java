package cl.duoc.servicioDePagoMS.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.servicioDePagoMS.model.ServicioDePago;
import cl.duoc.servicioDePagoMS.service.SDPservice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/pagos")
@Tag(
    name = "pagos",
    description = "relacion con los pagos"
)
@Schema(
    description = "información de pago dentro del sistema"
)
public class SDPcontroller {

    private final SDPservice service;
    public SDPcontroller(SDPservice service) {
    this.service = service;
    }
    @GetMapping 
    public ResponseEntity<List<ServicioDePago>> listaPagos(){
    List<ServicioDePago> listaPagos = service.listaPagos();
        if(listaPagos.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(listaPagos);
        }
    }
    @PostMapping
    public ResponseEntity<ServicioDePago> guardar(@RequestBody ServicioDePago pago){
        try {
            service.guardarPago(pago);
            return ResponseEntity.ok(service.guardarPago(pago));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

   
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ServicioDePago>> buscarPorOrderId(@PathVariable Integer orderId) {
                List<ServicioDePago> pagos = service.buscarPorOrderId(orderId);
                if (pagos.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(pagos);
        }
        @Operation(
            summary = "Obtener monto por ID",
            description = "Retorna el monto asociado a un pago según su ID")
            @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Monto encontrado"),
                @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
                @ApiResponse(responseCode = "500", description = "Error interno")
            })
            @GetMapping("/{id}/monto")
            public ResponseEntity<Double> obtenerMonto(
                @Parameter(
                    description = "ID del pago",
                    required = true,
                    example = "1"
                )
                @PathVariable Integer id) {
                    try {
                        return ResponseEntity.ok(service.obtenerMontoPorId(id));
                    } catch (Exception e) {
                        return ResponseEntity.notFound().build();
                    }
                }
}
