package cl.duoc.UsuariosMs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.UsuariosMs.model.Usuarios;
import cl.duoc.UsuariosMs.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuariosServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuariosService usuarioService;

    private Usuarios usuarios;

    @BeforeEach// Cambiado a BeforeEach para la inicialización
    void setUp() {
        usuarios = new Usuarios();
        usuarios.setId(1);
        usuarios.setNombre("Angel");
        usuarios.setEmail("angel@duocuc.cl");
        usuarios.setPassword("123456");
    }
    
    @Test
    void listar(){
        List<Usuarios> listaFalsa = new ArrayList<>();
        listaFalsa.add(usuarios);
        when(usuarioRepository.findAll()).thenReturn(listaFalsa);
        List<Usuarios> listaUsuarios = usuarioService.listarTodos();

        assertEquals(1, listaUsuarios.size());
        assertEquals("Angel", listaUsuarios.get(0).getNombre());
        }

    @Test
    void buscarPorId() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarios));
        Usuarios usuarioEncontrado = usuarioService.buscarPorId(1);

        assertNotNull(usuarioEncontrado);
        assertEquals("Angel", usuarioEncontrado.getNombre());
    }
    

    
}
