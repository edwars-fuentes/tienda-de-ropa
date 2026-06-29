package cl.duoc.UsuariosMs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import cl.duoc.UsuariosMs.model.Usuarios;
import cl.duoc.UsuariosMs.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuariosService implements CommandLineRunner {
    

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuarios> listarTodos() { 
        return usuarioRepository.findAll(); 
    }

    public Usuarios guardar(Usuarios usuario) {
        return usuarioRepository.save(usuario); 
    }

    public Usuarios buscarPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Usuarios u1 = new Usuarios();
            u1.setNombre("Angel");
            u1.setEmail("angel@duocuc.cl");
            u1.setPassword("123456");
            usuarioRepository.save(u1);

            Usuarios u2 = new Usuarios();
            u2.setNombre("Juan");
            u2.setEmail("juan@duocuc.cl");
            u2.setPassword("654321");
            usuarioRepository.save(u2);

            Usuarios u3 = new Usuarios();
            u3.setNombre("Bastián Aveldaño");
            u3.setEmail("bastian.avel@duocuc.cl");
            u3.setPassword("987654");
            usuarioRepository.save(u3);

            System.out.println("======> DataLoader Usuarios ejecutado");
        }
    }
}
