package cl.duoc.PromocionesMS.Controller;

import static org.mockito.ArgumentMatchers.any;
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

import cl.duoc.PromocionesMS.Model.Promocion;
import cl.duoc.PromocionesMS.Service.PromocionService;

@ExtendWith(MockitoExtension.class)
class PromocionControllerTest {

    @Mock
    private PromocionService service;

    @InjectMocks
    private PromocionController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Promocion sample;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        sample = new Promocion();
        sample.setId(1);
        sample.setCodigo("DUOC2026");
        sample.setDescuento(20);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listar_returnsOkAndList() throws Exception {
        when(service.listarTodas()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/v1/promociones"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].codigo").value("DUOC2026"))
            .andExpect(jsonPath("$[0].descuento").value(20));

        verify(service).listarTodas();
    }

    @Test
    void listar_returnsNoContentWhenEmpty() throws Exception {
        when(service.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/promociones"))
            .andExpect(status().isNoContent());

        verify(service).listarTodas();
    }

    @Test
    void crear_returnsCreatedAndLocation() throws Exception {
        Promocion input = new Promocion();
        input.setCodigo("NUEVA");
        input.setDescuento(15);

        when(service.guardar(any(Promocion.class))).thenAnswer(invocation -> {
            Promocion p = invocation.getArgument(0);
            p.setId(5);
            return p;
        });

        mockMvc.perform(post("/api/v1/promociones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/promociones/5")))
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.codigo").value("NUEVA"))
            .andExpect(jsonPath("$.descuento").value(15));

        verify(service).guardar(any(Promocion.class));
    }
}