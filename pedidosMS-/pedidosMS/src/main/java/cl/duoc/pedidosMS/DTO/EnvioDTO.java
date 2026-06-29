package cl.duoc.pedidosMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioDTO {
    private Integer id;

    private Integer orderId;
    private String direccion;
    private String estadoEnvio;

}
