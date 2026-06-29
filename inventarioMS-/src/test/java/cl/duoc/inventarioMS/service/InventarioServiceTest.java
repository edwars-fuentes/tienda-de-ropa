package cl.duoc.inventarioMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import cl.duoc.inventarioMS.DTO.ProductoDTO;
import cl.duoc.inventarioMS.Model.Inventario;
import cl.duoc.inventarioMS.Repository.InventarioRepository;
import cl.duoc.inventarioMS.Service.InventarioService;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario sample;

    @BeforeEach
    void setUp() {
        sample = new Inventario();
        sample.setProductoId(5);
        sample.setStock(10);
    }

    @Test
    void listarTodos_returnsList() {
        when(inventarioRepository.findAll()).thenReturn(List.of(sample));

        List<Inventario> result = inventarioService.listarTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getProductoId());
        verify(inventarioRepository).findAll();
    }

    @Test
    void guardar_whenProductExists_savesAndReturns() {
        Inventario toSave = new Inventario();
        toSave.setProductoId(5);
        toSave.setStock(20);

        String expectedUrl = "http://localhost:8082/api/v1/productos/" + toSave.getProductoId();

        // Simular que el producto existe en productosMS
        when(restTemplate.getForObject(expectedUrl, ProductoDTO.class)).thenReturn(new ProductoDTO());

        // Simular repo.save que asigna un id
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(invocation -> {
            Inventario i = invocation.getArgument(0);
            i.setId(42);
            return i;
        });

        Inventario saved = inventarioService.guardar(toSave);

        assertNotNull(saved);
        assertEquals(42, saved.getId());
        verify(restTemplate).getForObject(expectedUrl, ProductoDTO.class);
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void guardar_whenProductNotFound_returnsNull_andDoesNotSave() {
        Inventario toSave = new Inventario();
        toSave.setProductoId(7);
        toSave.setStock(5);

        String expectedUrl = "http://localhost:8082/api/v1/productos/" + toSave.getProductoId();

        // Simular que productosMS devuelve null (producto no existe)
        when(restTemplate.getForObject(expectedUrl, ProductoDTO.class)).thenReturn(null);

        Inventario result = inventarioService.guardar(toSave);

        assertNull(result);
        verify(restTemplate).getForObject(expectedUrl, ProductoDTO.class);
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void guardar_whenRestTemplateThrows_returnsNull_andDoesNotSave() {
        Inventario toSave = new Inventario();
        toSave.setProductoId(8);
        toSave.setStock(3);

        String expectedUrl = "http://localhost:8082/api/v1/productos/" + toSave.getProductoId();

        when(restTemplate.getForObject(expectedUrl, ProductoDTO.class)).thenThrow(new RuntimeException("comms error"));

        Inventario result = inventarioService.guardar(toSave);

        assertNull(result);
        verify(restTemplate).getForObject(expectedUrl, ProductoDTO.class);
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void run_whenRepositoryEmpty_attemptsToSaveThreeInventarios() throws Exception {
        // repo.count() == 0 to trigger insertion
        when(inventarioRepository.count()).thenReturn(0L);

        // Simular que para los ids 1,2,3 productosMS responde OK
        when(restTemplate.getForObject("http://localhost:8082/api/v1/productos/1", ProductoDTO.class))
            .thenReturn(new ProductoDTO());
        when(restTemplate.getForObject("http://localhost:8082/api/v1/productos/2", ProductoDTO.class))
            .thenReturn(new ProductoDTO());
        when(restTemplate.getForObject("http://localhost:8082/api/v1/productos/3", ProductoDTO.class))
            .thenReturn(new ProductoDTO());

        // Simular save -> devolver la misma entidad (o asignar id)
        when(inventarioRepository.save(any(Inventario.class))).thenAnswer(invocation -> {
            Inventario i = invocation.getArgument(0);
            if (i.getId() == null) i.setId(100 + i.getProductoId());
            return i;
        });

        // Ejecutar el CommandLineRunner
        inventarioService.run();

        // Capturar las invocaciones a save
        ArgumentCaptor<Inventario> captor = ArgumentCaptor.forClass(Inventario.class);
        verify(inventarioRepository, times(3)).save(captor.capture());
    }
}