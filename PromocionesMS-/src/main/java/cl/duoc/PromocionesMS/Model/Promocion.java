package cl.duoc.PromocionesMS.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_promociones")
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer id;
    
    @Schema(
        description = "Codigo del pago",
        example = "1000000"
    )
    @Column(nullable = false, unique = true)
    private String codigo;

    @Schema(
        description = "descuento del producto",
        example = "1"
    )
     @Column(nullable = false)
    private Integer descuento;
}
/*    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer id;

    @Column(name = "codigo_promocion", nullable = false, unique = true)
    private String codigo;

    @Column(name = "porcentaje_descuento", nullable = false)
    private Integer descuento; */