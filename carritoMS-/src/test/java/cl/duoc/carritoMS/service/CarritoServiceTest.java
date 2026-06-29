package cl.duoc.carritoMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import cl.duoc.carritoMS.DTO.ProductoDTO;
import cl.duoc.carritoMS.DTO.UsuarioDTO;
import cl.duoc.carritoMS.Model.Carrito;
import cl.duoc.carritoMS.Repository.CarritoRepository;
import cl.duoc.carritoMS.Service.CarritoService;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CarritoService carritoService;

    private Carrito c;

    @BeforeEach
    void setUp() {
        c = new Carrito();
        c.setUsuarioId(1);
        c.setProductoId(2);
        c.setCantidad(1);
    }

    @Test
    void listarTodo_returnsListFromRepository() {
        when(carritoRepository.findAll()).thenReturn(List.of(c));

        List<Carrito> result = carritoService.listarTodo();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getUsuarioId());
        verify(carritoRepository).findAll();
    }

    @Test
    void guardar_returnsSavedWhenUserAndProductExist() {
        UsuarioDTO usuario = new UsuarioDTO(1, "TestUser", "test@example.com", "secret");
        ProductoDTO producto = new ProductoDTO(2, "ProductoTest", 999.0, "Categoria");

        String urlUsuario = "http://localhost:8081/api/v1/usuarios/" + c.getUsuarioId();
        String urlProducto = "http://localhost:8082/api/v1/productos/" + c.getProductoId();

        when(restTemplate.getForObject(urlUsuario, UsuarioDTO.class)).thenReturn(usuario);
        when(restTemplate.getForObject(urlProducto, ProductoDTO.class)).thenReturn(producto);

        Carrito saved = new Carrito();
        saved.setUsuarioId(c.getUsuarioId());
        saved.setProductoId(c.getProductoId());
        saved.setCantidad(c.getCantidad());

        when(carritoRepository.save(c)).thenReturn(saved);

        Carrito result = carritoService.guardar(c);

        assertNotNull(result);
        assertEquals(c.getUsuarioId(), result.getUsuarioId());
        assertEquals(c.getProductoId(), result.getProductoId());
        verify(restTemplate).getForObject(urlUsuario, UsuarioDTO.class);
        verify(restTemplate).getForObject(urlProducto, ProductoDTO.class);
        verify(carritoRepository).save(c);
    }

    @Test
    void guardar_returnsNullWhenUserOrProductIsNull() {
        // Caso usuario null
        when(restTemplate.getForObject(anyString(), eq(UsuarioDTO.class))).thenReturn(null);
        when(restTemplate.getForObject(anyString(), eq(ProductoDTO.class))).thenReturn(new ProductoDTO());

        Carrito result1 = carritoService.guardar(c);
        assertNull(result1);
        verify(restTemplate).getForObject("http://localhost:8081/api/v1/usuarios/" + c.getUsuarioId(), UsuarioDTO.class);

        reset(restTemplate);

        // Caso producto null
        when(restTemplate.getForObject("http://localhost:8081/api/v1/usuarios/" + c.getUsuarioId(), UsuarioDTO.class))
            .thenReturn(new UsuarioDTO(1, "U", "u@x.com", "p"));
        when(restTemplate.getForObject("http://localhost:8082/api/v1/productos/" + c.getProductoId(), ProductoDTO.class))
            .thenReturn(null);

        Carrito result2 = carritoService.guardar(c);
        assertNull(result2);
        verify(restTemplate).getForObject("http://localhost:8082/api/v1/productos/" + c.getProductoId(), ProductoDTO.class);
    }
}