package cl.duoc.PromocionesMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.PromocionesMS.Model.Promocion;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
}
