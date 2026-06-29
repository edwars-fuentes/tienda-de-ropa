package cl.duoc.servicioDeEnviosMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.servicioDeEnviosMS.model.Envio;
import cl.duoc.servicioDeEnviosMS.repository.EnvioRepository;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository repo;
    

    public List<Envio> listaEnvios(){
        return repo.findAll();
    }

    public Envio guardarEnvio(Envio envio){
        return repo.save(envio);
    }

    public Envio buscarPorId(Integer id){
        return repo.findById(id).orElseThrow(()->new RuntimeException("producto no encontrado"));
    }

    public List<Envio> buscarPorOrderId(Integer orderId){
    return repo.findByOrderId(orderId);
    }

    public Envio actualizarEstado(Integer id, String nuevoEstado){

    Envio envio = repo.findById(id).orElseThrow(() -> new RuntimeException("Envio no encontrado"));

    envio.setEstadoEnvio(nuevoEstado);

    return repo.save(envio);
    }

    

}
