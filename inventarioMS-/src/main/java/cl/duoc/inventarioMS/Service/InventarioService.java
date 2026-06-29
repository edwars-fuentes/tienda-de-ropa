package cl.duoc.inventarioMS.Service;

import org.springframework.boot.CommandLineRunner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; 

import cl.duoc.inventarioMS.DTO.ProductoDTO;
import cl.duoc.inventarioMS.Model.Inventario;
import cl.duoc.inventarioMS.Repository.InventarioRepository;

@Service
public class InventarioService implements CommandLineRunner {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private RestTemplate restTemplate; 

    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }
    
    public Inventario guardar(Inventario inventario) {
        try {
            String url = "http://localhost:8082/api/v1/productos/" + inventario.getProductoId();
            
            ProductoDTO producto = restTemplate.getForObject(url, ProductoDTO.class);

            if (producto != null) {
                return inventarioRepository.save(inventario);
            }
        } catch (Exception e) {
            System.out.println("Error de comunicación: productosMS no responde o el producto no existe.");
        }
        return null;
    }
@Override
public void run(String... args) throws Exception {
    if (inventarioRepository.count() == 0) {
        
        Inventario i1 = new Inventario();
        i1.setProductoId(1);
        i1.setStock(100);
        this.guardar(i1);

    
        Inventario i2 = new Inventario();
        i2.setProductoId(2);
        i2.setStock(45);
        this.guardar(i2); 

        Inventario i3 = new Inventario();
        i3.setProductoId(3);
        i3.setStock(20);
        this.guardar(i3);

        System.out.println("======> DataLoader Inventario ejecutado");
        }
    }
}