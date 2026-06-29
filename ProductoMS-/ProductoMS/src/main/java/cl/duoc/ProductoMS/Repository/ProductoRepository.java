package cl.duoc.ProductoMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.ProductoMS.Model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}