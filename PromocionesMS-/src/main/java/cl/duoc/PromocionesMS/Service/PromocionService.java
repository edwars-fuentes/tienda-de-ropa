package cl.duoc.PromocionesMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import cl.duoc.PromocionesMS.Model.Promocion;
import cl.duoc.PromocionesMS.Repository.PromocionRepository;

@Service
public class PromocionService implements CommandLineRunner {

    @Autowired
    private PromocionRepository promocionRepository;

    public List<Promocion> listarTodas() {
        return promocionRepository.findAll();
    }

    public Promocion guardar(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    @Override
    public void run(String... args) throws Exception {
        if (promocionRepository.count() == 0) {
            Promocion pr1 = new Promocion();
            pr1.setCodigo("DUOC2026");
            pr1.setDescuento(20);
            promocionRepository.save(pr1);

            Promocion pr2 = new Promocion();
            pr2.setCodigo("BIENVENIDA");
            pr2.setDescuento(10);
            promocionRepository.save(pr2);

            Promocion pr3 = new Promocion();
            pr3.setCodigo("CYBER20");
            pr3.setDescuento(20);
            promocionRepository.save(pr3);
        }
    }
}