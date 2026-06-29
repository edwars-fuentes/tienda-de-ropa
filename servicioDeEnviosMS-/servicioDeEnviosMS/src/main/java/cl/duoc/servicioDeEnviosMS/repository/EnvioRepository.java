package cl.duoc.servicioDeEnviosMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.servicioDeEnviosMS.model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio,Integer> {

    List<Envio> findByOrderId(Integer orderId);

}


