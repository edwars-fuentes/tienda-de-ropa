package cl.duoc.inventarioMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.inventarioMS.Model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
