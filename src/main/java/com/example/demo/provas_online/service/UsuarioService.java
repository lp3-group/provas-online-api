package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.UsuarioJaExisteException;
import com.example.demo.provas_online.model.entity.Administrador;
import com.example.demo.provas_online.model.entity.Estudante;
import com.example.demo.provas_online.model.entity.Usuario;
import com.example.demo.provas_online.model.repository.AdministradorRepository;
import com.example.demo.provas_online.model.repository.EstudanteRepository;
import com.example.demo.provas_online.types.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private AuthService authService;

    public void validarESalvarUsuario (Usuario usuario) throws UsuarioJaExisteException {
        TipoUsuario tipoUsuario = usuario instanceof Administrador ? TipoUsuario.administrador : TipoUsuario.estudante;

        validarNomeUsuario(usuario.getNomeUsuario(), tipoUsuario);

        if(tipoUsuario.equals(TipoUsuario.estudante)) {
            Estudante estudante = (Estudante) usuario;
            validarMatricula(estudante.getMatricula());
        }

        usuario.setSenha(authService.encriptarSenha(usuario.getSenha()));

        salvar(usuario);
    }

    private void salvar(Usuario usuario) {
        if(usuario instanceof Administrador) {
            administradorRepository.save((Administrador) usuario);
            return;
        }

        estudanteRepository.save((Estudante) usuario);
    }

    private void validarNomeUsuario(String nomeUsuario, TipoUsuario tipo) throws UsuarioJaExisteException {
        Optional<Usuario> usuario = getUsuario(tipo.name(), nomeUsuario);

        if(usuario.isPresent()) {
            throw new UsuarioJaExisteException();
        }
    }

    private void validarMatricula(String matricula) throws UsuarioJaExisteException {
        Optional<Estudante> estudante = estudanteRepository.findByMatricula(matricula);

        if(estudante.isPresent()) {
            throw new UsuarioJaExisteException();
        }
    }

    public Optional getUsuario(String tipo, String nomeUsuario) {
        return ehAdministrador(tipo) ? getAdministrador(nomeUsuario) : getEstudante(nomeUsuario);
    }

    private boolean ehAdministrador(String tipo) {
        return tipo.equals("administrador");
    }

    private Optional<Administrador> getAdministrador(String nomeUsuario) {
        return administradorRepository.findByNomeUsuario(nomeUsuario);
    }

    private Optional<Estudante> getEstudante(String nomeUsuario) throws UsernameNotFoundException {
        return estudanteRepository.findByNomeUsuario(nomeUsuario);
    }
}
