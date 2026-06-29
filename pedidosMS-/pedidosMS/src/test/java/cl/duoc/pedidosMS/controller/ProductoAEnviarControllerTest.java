package cl.duoc.pedidosMS.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cl.duoc.pedidosMS.model.ProductoAEnviar;
import cl.duoc.pedidosMS.service.PedidosService;

@ExtendWith(MockitoExtension.class)
class ProductoAEnviarControllerTest {

    @Mock
    private PedidosService service;

    @InjectMocks
    private PedidoController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private ProductoAEnviar sample;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        sample = new ProductoAEnviar();
        sample.setId(1);
        sample.setStatus("PENDIENTE");
        sample.setTotal(100);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listaPAE_returnsOkAndJson() throws Exception {
        when(service.listaPAE()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/v1/productos_enviados"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].status").value("PENDIENTE"))
            .andExpect(jsonPath("$[0].total").value(100));

        verify(service).listaPAE();
    }

    @Test
    void listaPAE_noContent_returns204() throws Exception {
        when(service.listaPAE()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos_enviados"))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(service).listaPAE();
    }

    @Test
    void guardar_returnsOk_whenServiceSucceeds() throws Exception {
        ProductoAEnviar input = new ProductoAEnviar();
        input.setStatus("NUEVO");
        input.setTotal(250);

        when(service.guardar(any(ProductoAEnviar.class))).thenAnswer(invocation -> {
            ProductoAEnviar p = invocation.getArgument(0);
            p.setId(10);
            return p;
        });

        mockMvc.perform(post("/api/v1/productos_enviados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isOk()); // controller currently returns ok() without body

        verify(service, atLeastOnce()).guardar(any(ProductoAEnviar.class));
    }

    @Test
    void guardar_returnsBadRequest_whenServiceThrows() throws Exception {
        ProductoAEnviar input = new ProductoAEnviar();
        input.setStatus("NUEVO");
        input.setTotal(250);

        when(service.guardar(any(ProductoAEnviar.class))).thenThrow(new RuntimeException("error"));

        mockMvc.perform(post("/api/v1/productos_enviados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(service).guardar(any(ProductoAEnviar.class));
    }

    @Test
void buscarPorId_found_returnsOk() throws Exception {
    when(service.buscarPorId(1)).thenReturn(sample);
    MvcResult mvc = mockMvc.perform(get("/api/v1/productos_enviados/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.status").value("PENDIENTE"))
        .andExpect(jsonPath("$.total").value(100))
        .andReturn();
        Exception resolved = mvc.getResolvedException();
        if (resolved != null) {
            resolved.printStackTrace();
            fail("Controller lanzó excepción: " + resolved.getClass().getName() + " - " + resolved.getMessage());
        }
        verify(service, times(1)).buscarPorId(1);
    }

    @Test
    void buscarPorId_notFound_returns404() throws Exception {
        when(service.buscarPorId(99)).thenThrow(new RuntimeException("no encontrado"));

        mockMvc.perform(get("/api/v1/productos_enviados/99"))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(service).buscarPorId(99);
    }
}
