package cl.duoc.ProductoMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.ProductoMS.Model.Producto;
import cl.duoc.ProductoMS.Repository.ProductoRepository;
import cl.duoc.ProductoMS.Service.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto sample;

    @BeforeEach
    void setUp() {
        sample = new Producto();
        sample.setId(1);
        sample.setNombre("Polera Oversize Negra");
        sample.setPrecio(19990.0);
        sample.setCategoria("Ropa de Hombre");
    }

    @Test
    void listarTodos_returnsList() {
        when(productoRepository.findAll()).thenReturn(List.of(sample));

        List<Producto> result = productoService.listarTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Polera Oversize Negra", result.get(0).getNombre());
        verify(productoRepository).findAll();
    }

    @Test
    void guardar_savesAndReturns() {
        Producto toSave = new Producto();
        toSave.setNombre("Nuevo Producto");

        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            p.setId(42);
            return p;
        });

        Producto saved = productoService.guardar(toSave);

        assertNotNull(saved);
        assertEquals(42, saved.getId());
        assertEquals("Nuevo Producto", saved.getNombre());
        verify(productoRepository).save(toSave);
    }

    @Test
    void buscarPorId_found() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(sample));

        Producto found = productoService.buscarPorId(1);

        assertNotNull(found);
        assertEquals("Polera Oversize Negra", found.getNombre());
        verify(productoRepository).findById(1);
    }

    @Test
    void buscarPorId_notFound_returnsNull() {
        when(productoRepository.findById(2)).thenReturn(Optional.empty());

        Producto found = productoService.buscarPorId(2);

        assertNull(found);
        verify(productoRepository).findById(2);
    }

    @Test
    void run_insertsThreeProductsWhenCountIsZero() throws Exception {
        when(productoRepository.count()).thenReturn(0L);
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productoService.run();

        verify(productoRepository, times(3)).save(any(Producto.class));

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository, times(3)).save(captor.capture());
    }

    @Test
    void run_doesNotInsertWhenCountGreaterThanZero() throws Exception {
        when(productoRepository.count()).thenReturn(5L);

        productoService.run();

        verify(productoRepository, never()).save(any(Producto.class));
    }
}
