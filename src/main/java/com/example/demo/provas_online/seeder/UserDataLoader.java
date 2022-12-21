package com.example.demo.provas_online.seeder;

import com.example.demo.provas_online.model.entity.Administrador;
import com.example.demo.provas_online.model.entity.Usuario;
import com.example.demo.provas_online.model.repository.AdministradorRepository;
import com.example.demo.provas_online.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public void run(String... args) throws Exception {
        loadAdm();
    }

    private void loadAdm() {
        if (administradorRepository.count() == 0) {
            Administrador adm = Administrador.builder()
                    .nome("Administrador")
                    .nomeUsuario("admin")
                    .senha(encoder.encode("123456"))
                    .build();

            administradorRepository.save(adm);
        }
    }
}
