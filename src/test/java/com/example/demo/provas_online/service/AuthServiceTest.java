package com.example.demo.provas_online.service;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.example.demo.provas_online.api.dto.LoginDTO;
import com.example.demo.provas_online.interfaces.IUsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AuthServiceTest {
    @Test
    public void testaUsuarioNaoEncontrado() {
        IUsuarioService usuarioServiceMock = createMock(IUsuarioService.class);
        expect(usuarioServiceMock.getUsuario("")).andReturn(Optional.ofNullable(null));
        replay(usuarioServiceMock);

        LoginDTO dados = new LoginDTO("", "");

        try {
            AuthService authService = new AuthService();
            authService.autenticar(dados, usuarioServiceMock);
            fail();
        } catch (UsernameNotFoundException e) {
            assertEquals("Usuário não encontrado", e.getMessage());
        }
    }
}
