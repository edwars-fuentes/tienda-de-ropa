package cl.duoc.inventarioMS.Model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "inventario")
@Schema(description = "información del inventario")
public class Inventario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(
        description = "ID del producto",
        example = "1"
    )
    private Integer productoId;

    @Schema(
        description = "stock de los producto",
        example = "PAGADO"
    )
    private Integer stock;


}