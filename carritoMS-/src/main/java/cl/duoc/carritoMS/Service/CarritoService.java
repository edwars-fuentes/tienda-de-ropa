package cl.duoc.carritoMS.Service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cl.duoc.carritoMS.DTO.ProductoDTO;
import cl.duoc.carritoMS.DTO.UsuarioDTO;
import cl.duoc.carritoMS.Model.Carrito;
import cl.duoc.carritoMS.Repository.CarritoRepository;
import java.util.List;

@Service
public class CarritoService implements CommandLineRunner {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Carrito> listarTodo() {
        return carritoRepository.findAll();
    }

    public Carrito guardar(Carrito carrito) {
        String urlUsuario = "http://localhost:8081/api/v1/usuarios/" + carrito.getUsuarioId();
        String urlProducto = "http://localhost:8082/api/v1/productos/" + carrito.getProductoId();

        try {
            UsuarioDTO usuario = restTemplate.getForObject(urlUsuario, UsuarioDTO.class);
            ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);

            if (usuario != null && producto != null) {
                return carritoRepository.save(carrito);
            } else {
                System.out.println("Error: El Usuario o el Producto no existen.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error de comunicacion: Un microservicio no responde.");
            e.printStackTrace(); 
            
            return null;
        }
    }

    @Override
public void run(String... args) throws Exception {
    if (carritoRepository.count() == 0) {
        
        Carrito c1 = new Carrito();
        c1.setUsuarioId(1);
        c1.setProductoId(1);
        c1.setCantidad(1);
        this.guardar(c1);

        Carrito c2 = new Carrito();
        c2.setUsuarioId(1);
        c2.setProductoId(2);
        c2.setCantidad(2);
        this.guardar(c2);

        Carrito c3 = new Carrito();
        c3.setUsuarioId(2);
        c3.setProductoId(3);
        c3.setCantidad(1);
        this.guardar(c3);

        System.out.println("======> DataLoader Carrito ejecutado de manera correcta.");
        }
    }  
}