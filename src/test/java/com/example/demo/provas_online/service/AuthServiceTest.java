package com.example.demo.provas_online.service;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.provas_online.api.dto.LoginDTO;
import com.example.demo.provas_online.exception.SenhaInvalidaException;
import com.example.demo.provas_online.exception.UsuarioNaoExisteException;
import com.example.demo.provas_online.interfaces.IUsuarioService;
import com.example.demo.provas_online.model.entity.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTest {
    private PasswordEncoder passwordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence charSequence) {
            return null;
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return false;
        }
    };

    private AuthService authService;
    private UsuarioService usuarioService;

    @BeforeAll
    void init() {
        this.authService = new AuthService();
        this.usuarioService = new UsuarioService();
    }

    @Test
    public void testaUsuarioNaoEncontrado() {
        IUsuarioService usuarioServiceMock = createMock(IUsuarioService.class);
        expect(usuarioServiceMock.getUsuario("")).andReturn(Optional.ofNullable(null));
        replay(usuarioServiceMock);

        LoginDTO dados = new LoginDTO("", "");

        try {
            this.authService.autenticar(dados, usuarioServiceMock, this.passwordEncoder);
            fail();
        } catch (UsernameNotFoundException e) {
            assertEquals("Usuário não encontrado", e.getMessage());
        }
    }

    @Test
    public void testaSenhasInvalidas() {
        Usuario usuario = new Usuario();

        UserDetails userDetails = createMock(UserDetails.class);
        expect(userDetails.getPassword()).andReturn(null);
        replay(userDetails);

        IUsuarioService usuarioServiceMock = createMock(IUsuarioService.class);
        expect(usuarioServiceMock.getUsuario(anyObject())).andReturn(Optional.ofNullable(usuario));
        expect(usuarioServiceMock.loadUserByUsername(anyObject())).andReturn(userDetails);
        replay(usuarioServiceMock);

        LoginDTO dados = new LoginDTO("", "");

        try {
            this.authService.autenticar(dados, usuarioServiceMock, this.passwordEncoder);
            fail();
        } catch (SenhaInvalidaException e) {
            assertEquals(e.getClass(), new SenhaInvalidaException().getClass());
        }
    }

    @Test
    public void testaUsuarioEncontrado() {
        UserDetails userDetails = createMock(UserDetails.class);

        IUsuarioService usuarioServiceMock = createMock(UsuarioService.class);
        expect(usuarioServiceMock.getUsuario(new String())).andReturn(Optional.ofNullable(new Usuario()));
        expect(usuarioServiceMock.loadUserByUsername(anyObject())).andReturn(userDetails);
        replay(usuarioServiceMock);

        PasswordEncoder passwordEncoderMock = createMock(PasswordEncoder.class);
        expect(passwordEncoderMock.matches(anyObject(String.class), anyObject(String.class))).andReturn(true);
        replay(passwordEncoderMock);

        LoginDTO dados = new LoginDTO("", "");

        Usuario retorno = this.authService.autenticar(dados, usuarioServiceMock, passwordEncoderMock);

        assertInstanceOf(Usuario.class, retorno);
    }

    @Test
    public void testaValidarSenha() {
        try {
            this.authService.validarSenha("a", "b");
            fail();
        } catch (SenhaInvalidaException e) {
            assertEquals("As senhas não coincidem!", e.getMessage());
        }
    }

    @Test
    public void testaNaoEncontrarUsuarioAlteracaoSenha() {
        IUsuarioService usuarioServiceMock = createMock(UsuarioService.class);
        expect(usuarioServiceMock.getUsuario(new String())).andReturn(Optional.ofNullable(null));
        replay(usuarioServiceMock);

        try {
            this.authService.alterarSenha(new String(), new String(), usuarioServiceMock, this.passwordEncoder);
            fail();
        } catch (UsuarioNaoExisteException e) {
            assertEquals("Este usuário não existe", e.getMessage());
        }
    }

    @Test
    public void testaAlteracaoSenha() {
        IUsuarioService usuarioServiceMock = createMock(UsuarioService.class);
        expect(usuarioServiceMock.getUsuario(new String())).andReturn(Optional.ofNullable(new Usuario()));
        usuarioServiceMock.salvar(Usuario.builder().primeiroAcesso(false).build());
        expectLastCall().once();
        replay(usuarioServiceMock);

        this.authService.alterarSenha(new String(), new String(), usuarioServiceMock, this.passwordEncoder);

        verify(usuarioServiceMock);
    }
}
