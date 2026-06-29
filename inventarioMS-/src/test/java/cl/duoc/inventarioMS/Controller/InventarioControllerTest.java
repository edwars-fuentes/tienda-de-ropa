package cl.duoc.inventarioMS.Controller;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cl.duoc.inventarioMS.Model.Inventario;
import cl.duoc.inventarioMS.Service.InventarioService;

@ExtendWith(MockitoExtension.class)
class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Inventario inventario;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        inventario = new Inventario();
        inventario.setId(1);
        inventario.setProductoId(5);
        inventario.setStock(100);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ===== GET /api/v1/inventario =====

    @Test
    void listar201() throws Exception {
        when(inventarioService.listarTodos()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/api/v1/inventario"))
            .andDo(print())
            .andExpect(status().isOk()) // 200
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].productoId").value(5))
            .andExpect(jsonPath("$[0].stock").value(100));

        verify(inventarioService).listarTodos();
    }

    @Test
    void listar200() throws Exception {
        when(inventarioService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/inventario"))
            .andDo(print())
            .andExpect(status().isOk()) // 200
            .andExpect(content().json("[]"));

        verify(inventarioService).listarTodos();
    }

    // ===== POST /api/v1/inventario =====

    @Test
    void guardar200() throws Exception {
        Inventario inve = new Inventario();
        inve.setProductoId(7);
        inve.setStock(20);

        Inventario guardado = new Inventario();
        guardado.setId(10);
        guardado.setProductoId(7);
        guardado.setStock(20);

        when(inventarioService.guardar(any(Inventario.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(inve)))
            .andDo(print())
            .andExpect(status().isCreated()) // 201
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.productoId").value(7))
            .andExpect(jsonPath("$.stock").value(20));

        verify(inventarioService).guardar(any(Inventario.class));
    }


    @Test

    void guardar_returnsBadRequest_whenServiceReturnsNull() throws Exception {

        Inventario inve = new Inventario();
        inve.setProductoId(9);
        inve.setStock(5);
        when(inventarioService.guardar(any(Inventario.class))).thenReturn(null);
        mockMvc.perform(post("/api/v1/inventario")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(inve)))
        .andDo(print())
        .andExpect(status().isBadRequest()) // 400
        .andExpect(content().string(org.hamcrest.Matchers.containsString("No se pudo registrar el inventario. Verifique el productoId.")));



        verify(inventarioService).guardar(any(Inventario.class));

    }

    // ===== Tests para 404 =====

    @Test
    void listar404() throws Exception {
        mockMvc.perform(get("/api/v1/inventario/nonexistent"))
            .andDo(print())
            .andExpect(status().isNotFound()); // 404
    }

    @Test
    void POST400() throws Exception {
        String malformedJson = "{\"productoId\": invalid}"; // JSON inválido

        mockMvc.perform(post("/api/v1/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
            .andDo(print())
            .andExpect(status().isBadRequest()); // 404
    }
}