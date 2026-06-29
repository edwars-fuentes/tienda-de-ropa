package cl.duoc.UsuariosMs.model;
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
@Table(name = "tbl_usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Schema(
        description = "gmail del usuario"
    )
    @Column(name = "email")
    private String email;

        @Schema(
        description = "nombre del usuario"
    )
    @Column(name = "nombre_usuario")
    private String nombre;


    @Schema(
        description = "contraseña del usuario",
        example = "Password123"
    )
    @Column(name = "password ")
    private String password;
}




