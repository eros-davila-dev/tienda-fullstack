package com.example.demo;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Solo creamos el usuario si no existe (para no duplicarlo)
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // Aquí la magia: ¡Encriptamos el "1234" antes de guardarlo!
            admin.setPassword(passwordEncoder.encode("1234"));

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado con éxito en la Base de Datos");
        }
    }
}