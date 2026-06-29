package cl.duoc.servicioDePagoMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.servicioDePagoMS.model.ServicioDePago;

@Repository
public interface SDPRepository extends JpaRepository<ServicioDePago,Integer>{
    
    List<ServicioDePago> findByOrderId(Integer orderId);

}
