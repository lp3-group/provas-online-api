package com.example.demo.provas_online.service;

import com.example.demo.provas_online.api.dto.LoginDTO;
import com.example.demo.provas_online.exception.SenhaInvalidaException;
import com.example.demo.provas_online.model.entity.Administrador;
import com.example.demo.provas_online.model.entity.Estudante;
import com.example.demo.provas_online.model.entity.Usuario;
import com.example.demo.provas_online.model.repository.AdministradorRepository;
import com.example.demo.provas_online.model.repository.EstudanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private EstudanteRepository estudanteRepository;

    public Usuario autenticar(String tipo, LoginDTO credenciais) throws IllegalArgumentException, UsernameNotFoundException, SenhaInvalidaException {
        validarParametroTipoLogin(tipo);

        Usuario usuario = getUsuario(tipo, credenciais.getNomeUsuario());

        UserDetails userDetails = getUserDetails(usuario);

        boolean senhasBatem = encoder.matches(credenciais.getSenha(), userDetails.getPassword());

        if(!senhasBatem) {
            throw new SenhaInvalidaException();
        }

        return usuario;
    }

    private static void validarParametroTipoLogin(String tipo) {
        if(!tipo.equals("administrador") && !tipo.equals("estudante")) {
            throw new IllegalArgumentException("Tipo de login inválido!");
        }
    }

    public Usuario getUsuario(String tipo, String nomeUsuario) {
        return ehAdministrador(tipo) ? getAdministrador(nomeUsuario) : getEstudante(nomeUsuario);
    }

    public UserDetails getUserDetails(Usuario usuario) {
        String[] roles = usuario instanceof Administrador ? new String[]{"ADMIN", "ESTUDANTE"} : new String[]{"ESTUDANTE"};

        return User
                .builder()
                .username(usuario.getNomeUsuario())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

    private boolean ehAdministrador(String tipo) {
        return tipo.equals("administrador");
    }

    private Administrador getAdministrador(String nomeUsuario) throws UsernameNotFoundException {
        return administradorRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    private Estudante getEstudante(String nomeUsuario) throws UsernameNotFoundException {
        return estudanteRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public String encriptarSenha(String senha) {
        return encoder.encode(senha);
    }
}
