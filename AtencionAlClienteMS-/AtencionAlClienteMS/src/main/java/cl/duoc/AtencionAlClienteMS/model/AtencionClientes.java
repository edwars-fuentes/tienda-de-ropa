package cl.duoc.AtencionAlClienteMS.model;

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
@Table(name = "Atencion_al_cliente")
public class AtencionClientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Schema(
        description = " userId del Cliente"
    )
    @Column(nullable = false)
    private Integer userId;

    @Schema(
        description = "Mensaje Del Cliente",
        example = "Resivido"
    )
    @Column(nullable = false)
    private String mensaje;

     @Schema(
        description = "Estado del ticket",
        example = "Aprovado"
    )
    @Column(nullable = false)
    private String estadoTicket;

        
    @Schema(
        description = "Fecha del mensaje",
        example = "Enviado"
    )
    @Column(nullable = false)
    private String fecha;



}
 