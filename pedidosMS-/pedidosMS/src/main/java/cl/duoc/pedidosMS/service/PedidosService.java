package cl.duoc.pedidosMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cl.duoc.pedidosMS.DTO.EnvioDTO;
import cl.duoc.pedidosMS.DTO.PagoDTO;
import cl.duoc.pedidosMS.DTO.ProductoDTO;
import cl.duoc.pedidosMS.DTO.UsuarioDTO;
import cl.duoc.pedidosMS.model.ProductoAEnviar;
import cl.duoc.pedidosMS.repository.AEnviarRepository;
@Service
public class PedidosService  {

    @Autowired
    private AEnviarRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    public List<ProductoAEnviar> listaPAE() {
        return repo.findAll();
    }

    public ProductoAEnviar guardar(ProductoAEnviar pae) {
        return repo.save(pae);
    }

    public ProductoAEnviar buscarPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public ProductoDTO obtenerProducto(Integer id) {
        String url = "http://localhost:8081/api/v1/productos/" + id;
        return restTemplate.getForObject(url, ProductoDTO.class);
    }

    public EnvioDTO obtenerEnvio(Integer id) {
        String url = "http://localhost:8087/api/v1/envios/" + id;
        return restTemplate.getForObject(url, EnvioDTO.class);
    }

    public PagoDTO obtenerPago(Integer id) {
        String url = "http://localhost:8085/api/v1/pagos/" + id;
        return restTemplate.getForObject(url, PagoDTO.class);
    }

    public UsuarioDTO obtenerUsuario(Integer id) {
        String url = "http://localhost:8080/api/v1/usuarios/" + id;
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
}

