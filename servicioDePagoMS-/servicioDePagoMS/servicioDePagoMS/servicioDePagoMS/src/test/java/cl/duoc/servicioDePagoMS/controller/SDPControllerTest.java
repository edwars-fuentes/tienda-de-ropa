package cl.duoc.servicioDePagoMS.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cl.duoc.servicioDePagoMS.model.ServicioDePago;
import cl.duoc.servicioDePagoMS.repository.SDPRepository;
import cl.duoc.servicioDePagoMS.service.SDPservice;

@WebMvcTest(SDPRepository.class)
public class SDPControllerTest {

    @MockitoBean
    private SDPservice sdpService;

    private ServicioDePago SDP;

    private MockMvc llamadaFalsa;

    @BeforeEach
    void setUp() {
        SDP = new ServicioDePago();
        SDP.setMonto(1000000);
        SDP.setEstadoDePago(null);
        SDP.setOrderId(1);

        llamadaFalsa = MockMvcBuilders.standaloneSetup(new SDPcontroller(sdpService)).build();
    }

    @Test
    void obtenerMonto200() throws Exception {

        when(sdpService.obtenerMontoPorId(1))
                .thenReturn(1000000.0);

        llamadaFalsa.perform(get("/api/v1/pagos/1/monto"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000000.0"));
        }

    @Test
    void obtenerMonto404() throws Exception {

        when(sdpService.obtenerMontoPorId(99))
                .thenThrow(new RuntimeException());

        llamadaFalsa.perform(get("/api/v1/pagos/99/monto"))
                .andExpect(status().isNotFound());
        }
    @Test
    void buscarPorOrderId200() throws Exception {

        List<ServicioDePago> lista = List.of(SDP);
        when(sdpService.buscarPorOrderId(1))
            .thenReturn(lista);
            
        llamadaFalsa.perform(get("/api/v1/pagos/order/1"))
        .andExpect(status().isOk());
        }
    @Test
    void buscarPorOrderId204() throws Exception {
        when(sdpService.buscarPorOrderId(99))
            .thenReturn(List.of());

    llamadaFalsa.perform(get("/api/v1/pagos/order/99"))
            .andExpect(status().isNoContent());
        }

}