package cl.duoc.PromocionesMS.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.PromocionesMS.Model.Promocion;
import cl.duoc.PromocionesMS.Repository.PromocionRepository;

@ExtendWith(MockitoExtension.class)
class PromocionServiceTest {

    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private PromocionService promocionService;

    private Promocion pr;

    @BeforeEach
    void setUp() {
        pr = new Promocion();
        pr.setCodigo("DUOC2026");
        pr.setDescuento(20);
    }

    @Test
    void listarTodas_returnsList() {
        when(promocionRepository.findAll()).thenReturn(List.of(pr));

        List<Promocion> result = promocionService.listarTodas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DUOC2026", result.get(0).getCodigo());

        verify(promocionRepository).findAll();
    }

    @Test
    void guardar_savesAndReturns() {
        Promocion input = new Promocion();
        input.setCodigo("NUEVA");
        input.setDescuento(15);

        when(promocionRepository.save(input)).thenAnswer(invocation -> {
            Promocion arg = invocation.getArgument(0);
            // si tu entidad tiene id generado, podrías setearlo aquí
            return arg;
        });

        Promocion saved = promocionService.guardar(input);

        assertNotNull(saved);
        assertEquals("NUEVA", saved.getCodigo());
        assertEquals(15, saved.getDescuento());

        verify(promocionRepository).save(input);
    }

    @Test
    void run_seedsWhenEmpty_callsSaveThreeTimes() throws Exception {
        when(promocionRepository.count()).thenReturn(0L);
        when(promocionRepository.save(any(Promocion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        promocionService.run();

        verify(promocionRepository, times(3)).save(any(Promocion.class));
        verify(promocionRepository).count();
    }

    @Test
    void run_doesNotSeedWhenNotEmpty() throws Exception {
        when(promocionRepository.count()).thenReturn(2L);

        promocionService.run();

        verify(promocionRepository, never()).save(any(Promocion.class));
        verify(promocionRepository).count();
    }
}