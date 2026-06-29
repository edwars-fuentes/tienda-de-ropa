package cl.duoc.pedidosMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.pedidosMS.model.AtencionPedido;

@Repository
public interface APedidoRepository extends JpaRepository<AtencionPedido,Integer>{
    

}
