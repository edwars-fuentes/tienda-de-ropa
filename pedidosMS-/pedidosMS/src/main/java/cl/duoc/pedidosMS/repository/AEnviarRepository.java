package cl.duoc.pedidosMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.pedidosMS.model.ProductoAEnviar;

@Repository
public interface AEnviarRepository extends JpaRepository<ProductoAEnviar, Integer> {


}
