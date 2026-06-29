package cl.duoc.ServicioReportesMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.ServicioReportesMS.model.Reportes;
import cl.duoc.ServicioReportesMS.repository.ReportesRepository;

@Service
public class ReportesService {
    @Autowired
    private ReportesRepository repo;

    public List<Reportes> listaReportes(){
        return repo.findAll();
    }

    public Reportes guardarReportes(Reportes repor){
        return repo.save(repor);
    }

    public Reportes buscarPorId(Integer id){
        return repo.findById(id).orElseThrow(()->new RuntimeException("Reporte no encontrado"));
    }
    public List<Reportes> buscarPorTipoDeReporte(String tipoDeReportes){
    return repo.findByTipoReportes(tipoDeReportes);
    }
    public Reportes actualizarReportes(Integer id, String tipoDeReportes){

    Reportes repor = repo.findById(id).orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
    repor.setTipoReportes(tipoDeReportes);
    return repo.save(repor);
    }


}
