package cl.duoc.ServicioReportesMS.Controller;

import static org.hamcrest.Matchers.containsString;
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

import cl.duoc.ServicioReportesMS.controller.ReportesController;
import cl.duoc.ServicioReportesMS.model.Reportes;
import cl.duoc.ServicioReportesMS.service.ReportesService;

@ExtendWith(MockitoExtension.class)
class ReportesControllerTest {

    @Mock
    private ReportesService service;

    @InjectMocks
    private ReportesController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Reportes r1;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        r1 = new Reportes();
        r1.setId(1);
        r1.setTipoReportes("VENTAS");

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarReportes_returns200AndList() throws Exception {
        when(service.listaReportes()).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/v1/reportes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].tipoReportes").value("VENTAS"));

        verify(service).listaReportes();
    }

    @Test
    void listarReportes_returns404WhenEmpty() throws Exception {
        when(service.listaReportes()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/reportes"))
            .andExpect(status().isNoContent());

        verify(service).listaReportes();
    }

    @Test
    void buscarPorId_found_returns200() throws Exception {
        when(service.buscarPorId(1)).thenReturn(r1);

        mockMvc.perform(get("/api/v1/reportes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.tipoReportes").value("VENTAS"));

        verify(service).buscarPorId(1);
    }

    @Test
    void buscarPorId_notFound_returns404() throws Exception {
        when(service.buscarPorId(99)).thenThrow(new RuntimeException("Reporte no encontrado"));

        mockMvc.perform(get("/api/v1/reportes/99"))
            .andExpect(status().isNotFound());

        verify(service).buscarPorId(99);
    }

    @Test
    void buscarPorTipo_returns200AndList() throws Exception {
        when(service.buscarPorTipoDeReporte("VENTAS")).thenReturn(List.of(r1));

        mockMvc.perform(get("/api/v1/reportes/tipo/VENTAS"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].tipoReportes").value("VENTAS"));

        verify(service).buscarPorTipoDeReporte("VENTAS");
    }

    @Test
    void buscarPorTipo_returns404WhenEmpty() throws Exception {
        when(service.buscarPorTipoDeReporte("X")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/reportes/tipo/X"))
            .andExpect(status().isNoContent());

        verify(service).buscarPorTipoDeReporte("X");
    }

    @Test
    void guardarReportes_returns201Created_andLocation() throws Exception {
        Reportes input = new Reportes();
        input.setTipoReportes("NUEVO");

        when(service.guardarReportes(any(Reportes.class))).thenAnswer(invocation -> {
            Reportes arg = invocation.getArgument(0);
            arg.setId(42);
            return arg;
        });

        mockMvc.perform(post("/api/v1/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/v1/reportes/42")))
            .andExpect(jsonPath("$.id").value(42))
            .andExpect(jsonPath("$.tipoReportes").value("NUEVO"));

        verify(service).guardarReportes(any(Reportes.class));
    }

    @Test
    void actualizarReportes_returns200WhenExists() throws Exception {
        Reportes body = new Reportes();
        body.setTipoReportes("RESUMEN");

        when(service.actualizarReportes(eq(1), eq("RESUMEN"))).thenReturn(new Reportes(1, "RESUMEN"));

        mockMvc.perform(put("/api/v1/reportes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.tipoReportes").value("RESUMEN"));

        verify(service).actualizarReportes(1, "RESUMEN");
    }

    @Test
    void actualizarReportes_notFound_returns404() throws Exception {
        Reportes body = new Reportes();
        body.setTipoReportes("X");

        when(service.actualizarReportes(eq(99), eq("X"))).thenThrow(new RuntimeException("Reporte no encontrado"));

        mockMvc.perform(put("/api/v1/reportes/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isNotFound());

        verify(service).actualizarReportes(99, "X");
    }
}
