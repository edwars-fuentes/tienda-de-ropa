package cl.duoc.ProductoMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import cl.duoc.ProductoMS.Model.Producto;
import cl.duoc.ProductoMS.Repository.ProductoRepository;

@Service
public class ProductoService implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }
    public Producto buscarPorId(Integer id) {
    return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void run(String... args) throws Exception {
    if (productoRepository.count() == 0) {
        Producto p1 = new Producto();
        p1.setNombre("Polera Oversize Negra");
        p1.setPrecio(19990.0);
        p1.setCategoria("Ropa de Hombre");
        productoRepository.save(p1);

        Producto p2 = new Producto();
        p2.setNombre("Camisa Negra");
        p2.setPrecio(9990.0);
        p2.setCategoria("Ropa de Niño");
        productoRepository.save(p2);

        Producto p3 = new Producto();
        p3.setNombre("Polerón Hoodie Gris");
        p3.setPrecio(34990.0);
        p3.setCategoria("Polerón Hoodie Gris");;
        productoRepository.save(p3);

        System.out.println("======> DataLoader Productos ejecutado");
        }
    }
}