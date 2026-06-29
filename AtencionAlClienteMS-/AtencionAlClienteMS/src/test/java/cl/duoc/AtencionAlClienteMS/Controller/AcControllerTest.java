package cl.duoc.AtencionAlClienteMS.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import cl.duoc.AtencionAlClienteMS.controller.AcController;
import cl.duoc.AtencionAlClienteMS.model.AtencionClientes;
import cl.duoc.AtencionAlClienteMS.service.AcService;

@ExtendWith(MockitoExtension.class)
class AcControllerTest {

    @Mock
    private AcService service;

    @InjectMocks
    private AcController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private AtencionClientes ticket;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        ticket = new AtencionClientes();
        ticket.setId(1);
        ticket.setUserId(10);
        ticket.setEstadoTicket("PENDIENTE");

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listaAC_returns200AndList() throws Exception {
        when(service.listaTickets()).thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/v1/Atencion_Clientes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].userId").value(10))
            .andExpect(jsonPath("$[0].estadoTicket").value("PENDIENTE"));

        verify(service).listaTickets();
    }

    @Test
    void listaAC_returns204WhenEmpty() throws Exception {
        when(service.listaTickets()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/Atencion_Clientes"))
            .andExpect(status().isNoContent());

        verify(service).listaTickets();
    }

    @Test
    void guardar_returns200WhenSuccessful() throws Exception {
        AtencionClientes input = new AtencionClientes();
        input.setUserId(20);
        input.setEstadoTicket("ABIERTO");

        AtencionClientes saved = new AtencionClientes();
        saved.setId(5);
        saved.setUserId(20);
        saved.setEstadoTicket("ABIERTO");

        when(service.guardarTickets(any(AtencionClientes.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/Atencion_Clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.userId").value(20))
            .andExpect(jsonPath("$.estadoTicket").value("ABIERTO"));

        verify(service).guardarTickets(any(AtencionClientes.class));
    }

    @Test
    void guardar_returns400WhenServiceThrows() throws Exception {
        AtencionClientes input = new AtencionClientes();
        input.setUserId(99);
        input.setEstadoTicket("X");

        when(service.guardarTickets(any(AtencionClientes.class))).thenThrow(new RuntimeException("error"));

        mockMvc.perform(post("/api/v1/Atencion_Clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());

        verify(service).guardarTickets(any(AtencionClientes.class));
    }

    @Test
    void buscarPorUserId_returns200AndList() throws Exception {
        when(service.buscarPorUserId(10)).thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/v1/Atencion_Clientes/user/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].userId").value(10))
            .andExpect(jsonPath("$[0].estadoTicket").value("PENDIENTE"));

        verify(service).buscarPorUserId(10);
    }

    @Test
    void buscarPorUserId_returns204WhenEmpty() throws Exception {
        when(service.buscarPorUserId(99)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/Atencion_Clientes/user/99"))
            .andExpect(status().isNoContent());

        verify(service).buscarPorUserId(99);
    }

    @Test
    void actualizarEstado_returns200WhenExists() throws Exception {
        AtencionClientes updated = new AtencionClientes();
        updated.setId(1);
        updated.setUserId(10);
        updated.setEstadoTicket("CERRADO");
        when(service.actualizarEstado(eq(1), eq("CERRADO"))).thenReturn(updated);
        mockMvc.perform(put("/api/v1/Atencion_Clientes/1/estado")
        .contentType(MediaType.TEXT_PLAIN)   // <-- enviar como plain text
        .content("CERRADO"))                 // <-- contenido sin comillas JSON
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.estadoTicket").value("CERRADO"));  
        verify(service).actualizarEstado(1, "CERRADO");

    }

    @Test
    void actualizarEstado_notFound_returns404() throws Exception {
        when(service.actualizarEstado(eq(50), eq("X"))).thenThrow(new RuntimeException("Tickets no encontrado"));   
        mockMvc.perform(put("/api/v1/Atencion_Clientes/50/estado")
           
        .contentType(MediaType.TEXT_PLAIN)   
        .content("X"))  
        .andExpect(status().isNotFound());
        verify(service).actualizarEstado(50, "X");

    }
}
