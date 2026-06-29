package cl.duoc.ServicioReportesMS.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.ServicioReportesMS.model.Reportes;
import cl.duoc.ServicioReportesMS.repository.ReportesRepository;
import cl.duoc.ServicioReportesMS.service.ReportesService;

@ExtendWith(MockitoExtension.class)
class ReportesServiceTest {

    @Mock
    private ReportesRepository repo;

    @InjectMocks
    private ReportesService service;

    private Reportes r1;
    private Reportes r2;

    @BeforeEach
    void setUp() {
        r1 = new Reportes();
        r1.setId(1);
        r1.setTipoReportes("VENTAS");

        r2 = new Reportes();
        r2.setId(2);
        r2.setTipoReportes("INVENTARIO");
    }

    @Test
    void listaReportes_returnsAll() {
        when(repo.findAll()).thenReturn(List.of(r1, r2));

        List<Reportes> result = service.listaReportes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("VENTAS", result.get(0).getTipoReportes());
        verify(repo).findAll();
    }

    @Test
    void guardarReportes_savesAndReturns() {
        Reportes toSave = new Reportes();
        toSave.setTipoReportes("TEST");

        when(repo.save(toSave)).thenAnswer(invocation -> {
            Reportes arg = invocation.getArgument(0);
            arg.setId(99);
            return arg;
        });

        Reportes saved = service.guardarReportes(toSave);

        assertNotNull(saved);
        assertEquals(99, saved.getId());
        assertEquals("TEST", saved.getTipoReportes());
        verify(repo).save(toSave);
    }

    @Test
    void buscarPorId_found_returnsReport() {
        when(repo.findById(1)).thenReturn(Optional.of(r1));

        Reportes found = service.buscarPorId(1);

        assertNotNull(found);
        assertEquals(1, found.getId());
        assertEquals("VENTAS", found.getTipoReportes());
        verify(repo).findById(1);
    }

    @Test
    void buscarPorId_notFound_throws() {
        when(repo.findById(5)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.buscarPorId(5));
        assertTrue(ex.getMessage().contains("Reporte no encontrado"));
        verify(repo).findById(5);
    }

    @Test
    void buscarPorTipoDeReporte_returnsMatchingList() {
        when(repo.findByTipoReportes("VENTAS")).thenReturn(List.of(r1));

        List<Reportes> result = service.buscarPorTipoDeReporte("VENTAS");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("VENTAS", result.get(0).getTipoReportes());
        verify(repo).findByTipoReportes("VENTAS");
    }

    @Test
    void actualizarReportes_updatesAndSaves() {
        when(repo.findById(1)).thenReturn(Optional.of(r1));
        when(repo.save(any(Reportes.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reportes updated = service.actualizarReportes(1, "RESUMEN");

        assertNotNull(updated);
        assertEquals(1, updated.getId());
        assertEquals("RESUMEN", updated.getTipoReportes());
        verify(repo).findById(1);
        verify(repo).save(r1);
    }

    @Test
    void actualizarReportes_notFound_throws() {
        when(repo.findById(10)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.actualizarReportes(10, "X"));
        assertTrue(ex.getMessage().contains("Reporte no encontrado"));
        verify(repo).findById(10);
        verify(repo, never()).save(any());
    }
}
