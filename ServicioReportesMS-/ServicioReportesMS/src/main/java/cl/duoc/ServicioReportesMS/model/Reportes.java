package cl.duoc.ServicioReportesMS.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Servicio_reportes")
public class Reportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name="tipo_reporte")
    @Schema(
        description = "Muestra el tipo de reporte"
    )
    private String tipoReportes;


    @Column(nullable = false)  
    @Schema(
        description = "Descripcion del reporte"
    )
     private String descripcion;

    @Column(nullable = false)  
    @Schema(
        description = "fecha del reporte",
        example = "Revisado"
    )
    private String fecha;

    @Column(nullable = false)  
    @Schema(
        description = "total de las ventas"
    )
    private double totalVentas;

    @Column(nullable = false)  
    @Schema(
        description = "total del pedido"
    )
    private Integer totalPedidos;
    public Reportes(Integer id, String tipoReportes) {
    
        this.id = id;
        this.tipoReportes = tipoReportes;
    }
}
