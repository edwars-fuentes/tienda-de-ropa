package cl.duoc.pedidosMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import cl.duoc.pedidosMS.DTO.EnvioDTO;
import cl.duoc.pedidosMS.DTO.PagoDTO;
import cl.duoc.pedidosMS.DTO.ProductoDTO;
import cl.duoc.pedidosMS.DTO.UsuarioDTO;
import cl.duoc.pedidosMS.model.ProductoAEnviar;
import cl.duoc.pedidosMS.repository.AEnviarRepository;

@ExtendWith(MockitoExtension.class)
class ProductoAEnviarServiceTest {

    @Mock
    private AEnviarRepository repo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PedidosService service;

    private ProductoAEnviar sample;

    @BeforeEach
    void setUp() {
        sample = new ProductoAEnviar();
        sample.setId(1);
        sample.setStatus("PENDIENTE");
        sample.setTotal(100); // usa el campo total en lugar de orderId
    }

    @Test
    void listaPAE_returnsList() {
        when(repo.findAll()).thenReturn(List.of(sample));

        List<ProductoAEnviar> result = service.listaPAE();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("PENDIENTE", result.get(0).getStatus());
        assertEquals(100, result.get(0).getTotal());
        verify(repo).findAll();
    }

    @Test
    void guardar_savesAndReturns() {
        ProductoAEnviar toSave = new ProductoAEnviar();
        toSave.setStatus("NUEVO");
        toSave.setTotal(200);

        when(repo.save(any(ProductoAEnviar.class))).thenAnswer(invocation -> {
            ProductoAEnviar p = invocation.getArgument(0);
            p.setId(42);
            return p;
        });

        ProductoAEnviar saved = service.guardar(toSave);

        assertNotNull(saved);
        assertEquals(42, saved.getId());
        assertEquals("NUEVO", saved.getStatus());
        assertEquals(200, saved.getTotal());
        verify(repo).save(toSave);
    }

    @Test
    void buscarPorId_found() {
        when(repo.findById(1)).thenReturn(Optional.of(sample));

        ProductoAEnviar found = service.buscarPorId(1);

        assertNotNull(found);
        assertEquals(1, found.getId());
        assertEquals("Producto Test".length() > 0 ? "PENDIENTE" : "PENDIENTE", found.getStatus()); // mantengo status esperado
        verify(repo).findById(1);
    }

    @Test
    void buscarPorId_notFound_throws() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.buscarPorId(99));
        assertEquals("Producto no encontrado", ex.getMessage());
        verify(repo).findById(99);
    }

    @Test
    void obtenerProducto_callsRestTemplate_andReturnsDto() {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(1);
        dto.setNombre("Producto Externo");

        String expectedUrl = "http://localhost:8081/api/v1/productos/1";
        when(restTemplate.getForObject(expectedUrl, ProductoDTO.class)).thenReturn(dto);

        ProductoDTO result = service.obtenerProducto(1);

        assertNotNull(result);
        assertEquals("Producto Externo", result.getNombre());
        verify(restTemplate).getForObject(expectedUrl, ProductoDTO.class);
    }

    @Test
    void obtenerEnvio_callsRestTemplate_andReturnsDto() {
        EnvioDTO dto = new EnvioDTO();
        dto.setId(1);
        dto.setEstadoEnvio("PENDIENTE");

        String expectedUrl = "http://localhost:8087/api/v1/envios/1";
        when(restTemplate.getForObject(expectedUrl, EnvioDTO.class)).thenReturn(dto);

        EnvioDTO result = service.obtenerEnvio(1);

        assertNotNull(result);
        assertEquals("PENDIENTE", result.getEstadoEnvio());
        verify(restTemplate).getForObject(expectedUrl, EnvioDTO.class);
    }

    @Test
    void obtenerPago_callsRestTemplate_andReturnsDto() {
        PagoDTO dto = new PagoDTO();
        dto.setId(1);
        dto.setEstadoDePago("PAGADO");

        String expectedUrl = "http://localhost:8085/api/v1/pagos/1";
        when(restTemplate.getForObject(expectedUrl, PagoDTO.class)).thenReturn(dto);

        PagoDTO result = service.obtenerPago(1);

        assertNotNull(result);
        assertEquals("PAGADO", result.getEstadoDePago());
        verify(restTemplate).getForObject(expectedUrl, PagoDTO.class);
    }

    @Test
    void obtenerUsuario_callsRestTemplate_andReturnsDto() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(1);
        dto.setNombre("Juan");

        String expectedUrl = "http://localhost:8080/api/v1/usuarios/1";
        when(restTemplate.getForObject(expectedUrl, UsuarioDTO.class)).thenReturn(dto);

        UsuarioDTO result = service.obtenerUsuario(1);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(restTemplate).getForObject(expectedUrl, UsuarioDTO.class);
    }
}