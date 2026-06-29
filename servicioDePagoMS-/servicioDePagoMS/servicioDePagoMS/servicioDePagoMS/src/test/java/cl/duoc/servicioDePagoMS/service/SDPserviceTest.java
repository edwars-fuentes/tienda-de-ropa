package cl.duoc.servicioDePagoMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.servicioDePagoMS.model.ServicioDePago;
import cl.duoc.servicioDePagoMS.repository.SDPRepository;

@ExtendWith(MockitoExtension.class)
class SDPserviceTest {

    @Mock
    private SDPRepository sdpRepository;

    @InjectMocks
    private SDPservice sdpService;

    private ServicioDePago SDP;

    @BeforeEach
    void setUp(){
        SDP = new ServicioDePago();
        SDP.setMonto(1000000);
        SDP.setEstadoDePago("PENDIENTE");
        SDP.setOrderId(1);
        SDP.setId(1); // si tu entidad tiene id
    }

    @Test
    void listar_returnsListFromRepository(){
        List<ServicioDePago> listaFalsa = new ArrayList<>();
        listaFalsa.add(SDP);

        when(sdpRepository.findAll()).thenReturn(listaFalsa);

        List<ServicioDePago> listaSDP = sdpService.listaPagos();

        assertNotNull(listaSDP);
        assertEquals(1, listaSDP.size());
        assertEquals(1000000, listaSDP.get(0).getMonto());
        verify(sdpRepository).findAll();
    }

    @Test
    void guardarPago_savesAndReturns() {
        ServicioDePago input = new ServicioDePago();
        input.setMonto(50000);
        input.setOrderId(2);
        input.setEstadoDePago("CREATED");

        when(sdpRepository.save(input)).thenAnswer(invocation -> {
            ServicioDePago arg = invocation.getArgument(0);
            arg.setId(10);
            return arg;
        });

        ServicioDePago saved = sdpService.guardarPago(input);

        assertNotNull(saved);
        assertEquals(10, saved.getId());
        assertEquals(50000, saved.getMonto());
        assertEquals(2, saved.getOrderId());
        verify(sdpRepository).save(input);
    }

    @Test
    void buscarPorId_encontrado(){
        Optional<ServicioDePago> optionalSDP = Optional.of(SDP);
        when(sdpRepository.findById(1)).thenReturn(optionalSDP);

        ServicioDePago resultado = sdpService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getOrderId());
        assertEquals(1000000, resultado.getMonto());
        verify(sdpRepository).findById(1);
    }

    @Test
    void buscarPorId_NoEncontrado() {
        when(sdpRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class, () -> {
            sdpService.buscarPorId(99);
        });

        // coincide con el mensaje lanzado en el servicio: "producto no encontrado"
        assertEquals("producto no encontrado", error.getMessage());
        verify(sdpRepository).findById(99);
    }

    @Test
    void obtenerMontoPorId_returnsAmount() {
        when(sdpRepository.findById(1)).thenReturn(Optional.of(SDP));

        double monto = sdpService.obtenerMontoPorId(1);

        assertEquals(1000000, monto);
        verify(sdpRepository).findById(1);
    }

    @Test
    void obtenerMontoPorId_notFound_throws() {
        when(sdpRepository.findById(50)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> sdpService.obtenerMontoPorId(50));
        assertEquals("Pago no encontrado", ex.getMessage());
        verify(sdpRepository).findById(50);
    }

    @Test
    void buscarPorOrderId_returnsList() {
        ServicioDePago p2 = new ServicioDePago();
        p2.setId(2);
        p2.setOrderId(1);
        p2.setMonto(2000);
        p2.setEstadoDePago("PAID");

        when(sdpRepository.findByOrderId(1)).thenReturn(List.of(SDP, p2));

        List<ServicioDePago> result = sdpService.buscarPorOrderId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getOrderId());
        verify(sdpRepository).findByOrderId(1);
    }
}