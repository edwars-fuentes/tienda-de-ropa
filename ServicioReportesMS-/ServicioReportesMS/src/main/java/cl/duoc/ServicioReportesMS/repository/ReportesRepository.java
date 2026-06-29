package cl.duoc.ServicioReportesMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.ServicioReportesMS.model.Reportes;

@Repository
public interface ReportesRepository extends JpaRepository<Reportes,Integer>{

    List<Reportes> findByTipoReportes(String tipoReportes);

}
