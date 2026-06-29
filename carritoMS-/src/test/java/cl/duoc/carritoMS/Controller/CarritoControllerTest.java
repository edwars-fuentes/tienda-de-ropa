package cl.duoc.carritoMS.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cl.duoc.carritoMS.Model.Carrito;
import cl.duoc.carritoMS.Service.CarritoService;

@ExtendWith(MockitoExtension.class)
class CarritoControllerTest {

    @Mock
    private CarritoService service;

    @InjectMocks
    private CarritoController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Carrito sample;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        sample = new Carrito();
        sample.setUsuarioId(1);
        sample.setProductoId(2);
        sample.setCantidad(1);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void guardar_returns201CreatedWhenSuccessful() throws Exception {
        Carrito input = new Carrito();
        input.setUsuarioId(1);
        input.setProductoId(2);
        input.setCantidad(1);

        when(service.guardar(any(Carrito.class))).thenReturn(sample);

        mockMvc.perform(post("/api/v1/carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.usuarioId").value(1))
            .andExpect(jsonPath("$.productoId").value(2))
            .andExpect(jsonPath("$.cantidad").value(1));

        verify(service).guardar(any(Carrito.class));
    }

    @Test
    void guardar_returns400BadRequestWhenServiceReturnsNull() throws Exception {
        Carrito input = new Carrito();
        input.setUsuarioId(99);
        input.setProductoId(99);
        input.setCantidad(1);

        when(service.guardar(any(Carrito.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").value("Error: No se pudo crear el carrito. Verifica el usuario o producto."));

        verify(service).guardar(any(Carrito.class));
    }

    @Test
    void guardar_returns400WhenUserNotFound() throws Exception {
        Carrito input = new Carrito();
        input.setUsuarioId(999);
        input.setProductoId(2);
        input.setCantidad(1);

        // Simular que el servicio devuelve null porque el usuario no existe
        when(service.guardar(any(Carrito.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());

        verify(service).guardar(any(Carrito.class));
    }

    @Test
    void guardar_returns400WhenProductNotFound() throws Exception {
        Carrito input = new Carrito();
        input.setUsuarioId(1);
        input.setProductoId(999);
        input.setCantidad(1);

        // Simular que el servicio devuelve null porque el producto no existe
        when(service.guardar(any(Carrito.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());

        verify(service).guardar(any(Carrito.class));
    }
}