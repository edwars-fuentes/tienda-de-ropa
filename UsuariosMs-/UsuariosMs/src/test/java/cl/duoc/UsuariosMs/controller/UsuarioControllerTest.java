package cl.duoc.UsuariosMs.controller;

import cl.duoc.UsuariosMs.model.Usuarios;
import cl.duoc.UsuariosMs.service.UsuariosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuariosService usuariosService;

    private MockMvc mockMvc;
    private Usuarios usuarios;

    @BeforeEach
    void setUp() {
        usuarios = new Usuarios();
        usuarios.setId(1);
        usuarios.setNombre("Angel");
        usuarios.setEmail("angel@duocuc.cl");
        usuarios.setPassword("123456");

        // Construir controller con el mock del servicio (inyección por constructor)
        UsuarioController controller = new UsuarioController(usuariosService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void buscarPorId() throws Exception {
        when(usuariosService.buscarPorId(1)).thenReturn(usuarios);

        mockMvc.perform(get("/api/v1/usuarios/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Angel"))
            .andExpect(jsonPath("$.email").value("angel@duocuc.cl"));

        verify(usuariosService).buscarPorId(1);
    }
}