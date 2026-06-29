package cl.duoc.pedidosMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {

    private Integer id;
    private double monto;
    private String estadoDePago;
    private String metodoDePago;
    private Integer ubicacionId;

}
