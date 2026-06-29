package cl.duoc.servicioDePagoMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.servicioDePagoMS.model.ServicioDePago;
import cl.duoc.servicioDePagoMS.repository.SDPRepository;

@Service
public class SDPservice {

    @Autowired
    private SDPRepository repo;

    public List<ServicioDePago> listaPagos(){
        return repo.findAll();
    }

    public ServicioDePago guardarPago(ServicioDePago SDA){
        return repo.save(SDA);
    }

    public ServicioDePago buscarPorId(Integer id){
        return repo.findById(id).orElseThrow(()->new RuntimeException("producto no encontrado"));
    }

    public double obtenerMontoPorId(Integer id){
    ServicioDePago pago = repo.findById(id).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    return pago.getMonto();
    }
    public List<ServicioDePago> buscarPorOrderId(Integer orderId){
    return repo.findByOrderId(orderId);
    }

}
