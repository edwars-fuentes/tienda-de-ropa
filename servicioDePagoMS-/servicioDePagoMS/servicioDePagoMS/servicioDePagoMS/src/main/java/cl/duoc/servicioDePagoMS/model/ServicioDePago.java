package cl.duoc.servicioDePagoMS.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicio_de_pago")
@Schema(description = "información de un pago en el sistema")
public class ServicioDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Schema(
        description = "Monto de pago",
        example = "1000000"
    )
    private double monto;

    @Schema(
        description = "ID asociada al pago",
        example = "1"
    )
    private Integer orderId;

    @Schema(
        description = "Estado del pago",
        example = "PAGADO"
    )
    private String estadoDePago;
}

