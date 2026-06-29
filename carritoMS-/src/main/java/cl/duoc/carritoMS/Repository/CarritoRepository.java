package cl.duoc.carritoMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.carritoMS.Model.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
}
