package cl.duoc.AtencionAlClienteMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cl.duoc.AtencionAlClienteMS.model.AtencionClientes;
import cl.duoc.AtencionAlClienteMS.DTO.UsuarioDTO;
import cl.duoc.AtencionAlClienteMS.repository.AcRepository;

@Service
public class AcService {

    @Autowired
    private AcRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    public List<AtencionClientes> listaTickets(){
        return repo.findAll();
    }
    public AtencionClientes guardarTickets(AtencionClientes Tickets){
        return repo.save(Tickets);
    }
    public AtencionClientes buscarPorId(Integer id){
        return repo.findById(id).orElseThrow(()->new RuntimeException("Tickets no encontrado"));
    }
    public List<AtencionClientes> buscarPorUserId(Integer userId){
    return repo.findByUserId(userId);
    }
    public AtencionClientes actualizarEstado(Integer id, String nuevoEstado){

    AtencionClientes ticket = repo.findById(id).orElseThrow(() -> new RuntimeException("Tickets no encontrado"));
    ticket.setEstadoTicket(nuevoEstado);
    return repo.save(ticket);
    }
    public UsuarioDTO obtenerUsuario(Integer id){
        String url ="http://localhost:8080/api/v1/usuarios/" + id;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }


}
