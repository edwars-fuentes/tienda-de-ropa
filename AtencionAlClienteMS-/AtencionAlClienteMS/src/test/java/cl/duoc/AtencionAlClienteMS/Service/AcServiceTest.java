package cl.duoc.AtencionAlClienteMS.Service;

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
import org.springframework.web.client.RestTemplate;

import cl.duoc.AtencionAlClienteMS.DTO.UsuarioDTO;
import cl.duoc.AtencionAlClienteMS.model.AtencionClientes;
import cl.duoc.AtencionAlClienteMS.repository.AcRepository;
import cl.duoc.AtencionAlClienteMS.service.AcService;

@ExtendWith(MockitoExtension.class)
class AcServiceTest {

    @Mock
    private AcRepository repo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AcService service;

    private AtencionClientes ticket;

    @BeforeEach
    void setUp() {
        ticket = new AtencionClientes();
        ticket.setId(1);
        ticket.setUserId(10);
        ticket.setEstadoTicket("PENDIENTE");
    }

    @Test
    void listaTickets_returnsList() {
        when(repo.findAll()).thenReturn(List.of(ticket));

        List<AtencionClientes> result = service.listaTickets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getUserId());
        verify(repo).findAll();
    }

    @Test
    void guardarTickets_savesAndReturns() {
        AtencionClientes input = new AtencionClientes();
        input.setUserId(20);
        input.setEstadoTicket("ABIERTO");

        when(repo.save(input)).thenAnswer(invocation -> {
            AtencionClientes arg = invocation.getArgument(0);
            arg.setId(5);
            return arg;
        });

        AtencionClientes saved = service.guardarTickets(input);

        assertNotNull(saved);
        assertEquals(5, saved.getId());
        assertEquals("ABIERTO", saved.getEstadoTicket());
        verify(repo).save(input);
    }

    @Test
    void buscarPorId_found_returnsTicket() {
        when(repo.findById(1)).thenReturn(Optional.of(ticket));

        AtencionClientes found = service.buscarPorId(1);

        assertNotNull(found);
        assertEquals(1, found.getId());
        assertEquals("PENDIENTE", found.getEstadoTicket());
        verify(repo).findById(1);
    }

    @Test
    void buscarPorId_notFound_throws() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.buscarPorId(99));
        assertTrue(ex.getMessage().contains("Tickets no encontrado"));
        verify(repo).findById(99);
    }

    @Test
    void buscarPorUserId_returnsList() {
        when(repo.findByUserId(10)).thenReturn(List.of(ticket));

        List<AtencionClientes> result = service.buscarPorUserId(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getUserId());
        verify(repo).findByUserId(10);
    }

    @Test
    void actualizarEstado_updatesAndSaves() {
        when(repo.findById(1)).thenReturn(Optional.of(ticket));
        when(repo.save(any(AtencionClientes.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AtencionClientes updated = service.actualizarEstado(1, "CERRADO");

        assertNotNull(updated);
        assertEquals(1, updated.getId());
        assertEquals("CERRADO", updated.getEstadoTicket());
        verify(repo).findById(1);
        verify(repo).save(ticket);
    }

    @Test
    void actualizarEstado_notFound_throws() {
        when(repo.findById(50)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.actualizarEstado(50, "X"));
        assertTrue(ex.getMessage().contains("Tickets no encontrado"));
        verify(repo).findById(50);
        verify(repo, never()).save(any());
    }

    @Test
    void obtenerUsuario_returnsDto() {
        UsuarioDTO usuario = new UsuarioDTO();
        // set fields if needed: usuario.setIdUsuario(...); etc.

        String url = "http://localhost:8080/api/v1/usuarios/" + 10;
        when(restTemplate.getForObject(url, UsuarioDTO.class)).thenReturn(usuario);

        UsuarioDTO result = service.obtenerUsuario(10);

        assertNotNull(result);
        verify(restTemplate).getForObject(url, UsuarioDTO.class);
    }

    @Test
    void obtenerUsuario_whenRestTemplateThrows_propagatesException() {
        String url = "http://localhost:8080/api/v1/usuarios/" + 999;
        when(restTemplate.getForObject(url, UsuarioDTO.class)).thenThrow(new RuntimeException("service down"));

        assertThrows(RuntimeException.class, () -> service.obtenerUsuario(999));
        verify(restTemplate).getForObject(url, UsuarioDTO.class);
    }
}
