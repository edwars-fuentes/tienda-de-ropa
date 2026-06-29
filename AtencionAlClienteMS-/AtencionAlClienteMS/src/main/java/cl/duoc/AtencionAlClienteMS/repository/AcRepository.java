package cl.duoc.AtencionAlClienteMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.AtencionAlClienteMS.model.AtencionClientes;

@Repository
public interface AcRepository extends JpaRepository<AtencionClientes,Integer> {
    List<AtencionClientes> findByUserId(Integer userId);
}
