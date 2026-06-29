package cl.duoc.servicioDeEnviosMS.Controller;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cl.duoc.servicioDeEnviosMS.controller.EnvioController;
import cl.duoc.servicioDeEnviosMS.model.Envio;
import cl.duoc.servicioDeEnviosMS.service.EnvioService;

@ExtendWith(MockitoExtension.class)
class EnvioControllerTest {

    @Mock
    private EnvioService service;

    @InjectMocks
    private EnvioController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Envio sample;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        sample = new Envio();
        sample.setId(1);
        sample.setOrderId(100);
        sample.setEstadoEnvio("PENDIENTE");

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listaEnvios_returnsOkAndList() throws Exception {
        when(service.listaEnvios()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/v1/Envios")) // Cambié a mayúscula E
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].orderId").value(100))
            .andExpect(jsonPath("$[0].estadoEnvio").value("PENDIENTE"));

        verify(service).listaEnvios();
    }

    @Test
    void listaEnvios_noContent_returns204() throws Exception {
        when(service.listaEnvios()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/Envios"))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(service).listaEnvios();
    }

    @Test
    void buscarPorId_found_returnsOk() throws Exception {
        when(service.buscarPorId(1)).thenReturn(sample);

        mockMvc.perform(get("/api/v1/Envios/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.orderId").value(100))
            .andExpect(jsonPath("$.estadoEnvio").value("PENDIENTE"));

        verify(service).buscarPorId(1);
    }

    @Test
    void buscarPorId_notFound_returns404() throws Exception {
        when(service.buscarPorId(99)).thenThrow(new RuntimeException("Envio no encontrado"));

        mockMvc.perform(get("/api/v1/Envios/99"))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(service).buscarPorId(99);
    }

    @Test
    void buscarPorOrderId_returnsOkAndList() throws Exception {
        when(service.buscarPorOrderId(100)).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/v1/Envios/order/100"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].orderId").value(100));

        verify(service).buscarPorOrderId(100);
    }

    @Test
    void guardar_returnsOkAndCreatedEnvio() throws Exception {
        Envio input = new Envio();
        input.setOrderId(200);
        input.setEstadoEnvio("PENDIENTE");

        when(service.guardarEnvio(any(Envio.class))).thenAnswer(invocation -> {
            Envio e = invocation.getArgument(0);
            e.setId(10);
            return e;
        });

        mockMvc.perform(post("/api/v1/Envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.orderId").value(200));

        verify(service, times(1)).guardarEnvio(any(Envio.class));
    }
}