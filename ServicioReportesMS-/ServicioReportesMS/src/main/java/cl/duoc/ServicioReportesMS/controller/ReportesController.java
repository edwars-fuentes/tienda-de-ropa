package cl.duoc.ServicioReportesMS.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.ServicioReportesMS.model.Reportes;
import cl.duoc.ServicioReportesMS.service.ReportesService;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReportesController {

    private ReportesService service;

    public ReportesController(ReportesService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Reportes>> listaReportes() {
        List<Reportes> lista = service.listaReportes();
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reportes> buscarPorId(@PathVariable Integer id) {
        try {
            Reportes r = service.buscarPorId(id);
            return ResponseEntity.ok(r);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reportes>> buscarPorTipo(@PathVariable("tipo") String tipo) {
        List<Reportes> lista = service.buscarPorTipoDeReporte(tipo);
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<Reportes> guardarReportes(@RequestBody Reportes repor) {
        Reportes saved = service.guardarReportes(repor);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reportes> actualizarReportes(@PathVariable Integer id, @RequestBody Reportes repor) {
        try {
            Reportes updated = service.actualizarReportes(id, repor.getTipoReportes());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}