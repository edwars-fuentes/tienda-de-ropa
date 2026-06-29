package cl.duoc.servicioDeEnviosMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.servicioDeEnviosMS.model.Envio;
import cl.duoc.servicioDeEnviosMS.repository.EnvioRepository;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository repo;

    @InjectMocks
    private EnvioService envioService;

    private Envio sample;

    @BeforeEach
    void setUp() {
        sample = new Envio();
        sample.setId(1);
        sample.setOrderId(100);
        sample.setEstadoEnvio("PENDIENTE");
        // si Envio tiene más campos, no es necesario inicializarlos para estos tests
    }

    @Test
    void listaEnvios_returnsList() {
        when(repo.findAll()).thenReturn(List.of(sample));

        List<Envio> result = envioService.listaEnvios();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getOrderId());
        verify(repo).findAll();
    }

    @Test
    void guardarEnvio_savesAndReturns() {
        Envio toSave = new Envio();
        toSave.setOrderId(200);
        toSave.setEstadoEnvio("PENDIENTE");

        when(repo.save(any(Envio.class))).thenAnswer(invocation -> {
            Envio e = invocation.getArgument(0);
            e.setId(42);
            return e;
        });

        Envio saved = envioService.guardarEnvio(toSave);

        assertNotNull(saved);
        assertEquals(42, saved.getId());
        assertEquals(200, saved.getOrderId());
        verify(repo).save(toSave);
    }

    @Test
    void buscarPorId_found() {
        when(repo.findById(1)).thenReturn(Optional.of(sample));

        Envio found = envioService.buscarPorId(1);

        assertNotNull(found);
        assertEquals(1, found.getId());
        assertEquals("PENDIENTE", found.getEstadoEnvio());
        verify(repo).findById(1);
    }

    @Test
    void buscarPorId_notFound_throws() {
        when(repo.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.buscarPorId(999));
        assertEquals("producto no encontrado", ex.getMessage());
        verify(repo).findById(999);
    }

    @Test
    void buscarPorOrderId_returnsList() {
        when(repo.findByOrderId(100)).thenReturn(List.of(sample));

        List<Envio> result = envioService.buscarPorOrderId(100);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getOrderId());
        verify(repo).findByOrderId(100);
    }

    @Test
    void actualizarEstado_updatesAndSaves() {
        when(repo.findById(1)).thenReturn(Optional.of(sample));
        when(repo.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Envio updated = envioService.actualizarEstado(1, "ENVIADO");

        assertNotNull(updated);
        assertEquals("ENVIADO", updated.getEstadoEnvio());

        ArgumentCaptor<Envio> captor = ArgumentCaptor.forClass(Envio.class);
        verify(repo).save(captor.capture());
        Envio saved = captor.getValue();
        assertEquals("ENVIADO", saved.getEstadoEnvio());

        verify(repo).findById(1);
    }

    @Test
    void actualizarEstado_notFound_throws() {
        when(repo.findById(555)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> envioService.actualizarEstado(555, "CANCELADO"));
        assertEquals("Envio no encontrado", ex.getMessage());
        verify(repo).findById(555);
        verify(repo, never()).save(any());
    }
}