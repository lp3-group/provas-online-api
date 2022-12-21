package com.example.demo.provas_online.service;

import com.example.demo.provas_online.api.dto.LoginDTO;
import com.example.demo.provas_online.exception.SenhaInvalidaException;
import com.example.demo.provas_online.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioService usuarioService;

    public Usuario autenticar(LoginDTO credenciais) throws IllegalArgumentException, UsernameNotFoundException, SenhaInvalidaException {
        Optional<Usuario> usuario = usuarioService.getUsuario(credenciais.getNomeUsuario());

        if(usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        UserDetails userDetails = usuarioService.loadUserByUsername(usuario.get().getNomeUsuario());

        boolean senhasBatem = encoder.matches(credenciais.getSenha(), userDetails.getPassword());

        if(!senhasBatem) {
            throw new SenhaInvalidaException();
        }

        return usuario.get();
    }
}
