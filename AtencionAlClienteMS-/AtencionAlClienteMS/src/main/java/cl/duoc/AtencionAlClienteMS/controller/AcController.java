package cl.duoc.AtencionAlClienteMS.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.AtencionAlClienteMS.model.AtencionClientes;
import cl.duoc.AtencionAlClienteMS.service.AcService;

@RestController
@RequestMapping("/api/v1/Atencion_Clientes")
public class AcController {
    private final AcService service;
    public AcController(AcService service) {
    this.service = service;
    }

    @GetMapping 
    public ResponseEntity<List<AtencionClientes>> listaAC(){
    List<AtencionClientes> listaAC = service.listaTickets();
        if(listaAC.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(listaAC);
        }
    }

    @PostMapping
    public ResponseEntity<AtencionClientes> guardar(@RequestBody AtencionClientes AC){
        try {
            AtencionClientes ticketGuardado = service.guardarTickets(AC);
            return ResponseEntity.ok(ticketGuardado);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AtencionClientes>> buscarPorUserId(@PathVariable Integer userId){

        List<AtencionClientes> tickets = service.buscarPorUserId(userId);
        if(tickets.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(tickets);
        }
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<AtencionClientes> actualizarEstado(
        @PathVariable Integer id,
        @RequestBody String nuevoEstado){
    try {
        AtencionClientes ticketActualizado =service.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(ticketActualizado);

        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    


}
