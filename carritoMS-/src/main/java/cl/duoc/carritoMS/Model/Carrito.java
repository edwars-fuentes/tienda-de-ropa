package cl.duoc.carritoMS.Model;

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
@Table(name = "tbl_carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Integer id;

    @Schema(
        description = "id del usuario"
    )
    @Column(name = "id_usuario", nullable = false)
    private Integer usuarioId;

    @Schema(
        description = "productoId"
    )
    @Column(name = "id_producto", nullable = false)
    private Integer productoId;

    @Schema(
        description = "cantidad del producto"
    )
    @Column(name = "cantidad_productos", nullable = false)
    private Integer cantidad;
}