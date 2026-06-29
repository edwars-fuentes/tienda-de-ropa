package cl.duoc.ProductoMS.Model;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicio_de_pago")
@Schema(description = "información de un pago en el sistema")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @Schema(
        description = "nombre del producto",
        example = "1"
    )
    private String nombre;

    @Schema(
        description = "precio_unitario",
        example = "PAGADO"
    )
    private Double precio;

    @Schema(
        description = "categoria",
        example = "conseguido"
    )
    private String categoria;
}
