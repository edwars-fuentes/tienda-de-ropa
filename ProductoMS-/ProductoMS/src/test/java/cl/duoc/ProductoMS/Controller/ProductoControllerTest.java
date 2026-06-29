package cl.duoc.ProductoMS.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import cl.duoc.ProductoMS.Model.Producto;
import cl.duoc.ProductoMS.Service.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Producto sample;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        sample = new Producto();
        sample.setId(1);
        sample.setNombre("Polera Oversize Negra");
        sample.setPrecio(19990.0);
        sample.setCategoria("Ropa de Hombre");
        mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();
    }

    @Test
    void listarTodos() throws Exception {
        when(productoService.listarTodos()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/v1/productos"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Polera Oversize Negra"))
            .andExpect(jsonPath("$[0].precio").value(19990.0));

        verify(productoService).listarTodos();
    }

    @Test
    void obtenerPorId() throws Exception {
        when(productoService.buscarPorId(1)).thenReturn(sample);

        mockMvc.perform(get("/api/v1/productos/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Polera Oversize Negra"));

        verify(productoService).buscarPorId(1);
    }

    @Test
    void obtenerPorId404() throws Exception {
        when(productoService.buscarPorId(99)).thenReturn(null);

        mockMvc.perform(get("/api/v1/productos/99"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().string("Error: El producto con ID 99 no existe."));

        verify(productoService).buscarPorId(99);
    }

    @Test
    void guardar() throws Exception {
        Producto input = new Producto();
        input.setNombre("Nuevo Producto");
        input.setPrecio(5000.0);
        input.setCategoria("Accesorios");

        // Simular que el repositorio asigna un id
        when(productoService.guardar(any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            p.setId(10);
            return p;
        });

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.nombre").value("Nuevo Producto"));

        verify(productoService).guardar(any(Producto.class));
    }
}