package com.example.demo.provas_online.service;

import com.example.demo.provas_online.exception.UsuarioJaExisteException;
import com.example.demo.provas_online.exception.UsuarioNaoExisteException;
import com.example.demo.provas_online.model.entity.Administrador;
import com.example.demo.provas_online.model.entity.Estudante;
import com.example.demo.provas_online.model.entity.Usuario;
import com.example.demo.provas_online.model.repository.AdministradorRepository;
import com.example.demo.provas_online.model.repository.EstudanteRepository;
import com.example.demo.provas_online.types.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private EstudanteRepository estudanteRepository;

    public void validarESalvarUsuario (Usuario usuario) throws UsuarioJaExisteException {
        TipoUsuario tipoUsuario = usuario instanceof Administrador ? TipoUsuario.administrador : TipoUsuario.estudante;

        validarNomeUsuario(usuario.getNomeUsuario(), tipoUsuario);

        if(tipoUsuario.equals(TipoUsuario.estudante)) {
            Estudante estudante = (Estudante) usuario;
            validarMatricula(estudante.getMatricula());
        }

        usuario.setSenha(encoder.encode(usuario.getSenha()));

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
        Optional<Usuario> usuario = getUsuario(nomeUsuario);

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = getUsuario(username);

        if(usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        String[] roles = usuario.get() instanceof Administrador ? new String[]{"ADMIN", "ESTUDANTE"} : new String[]{"ESTUDANTE"};

        return User
                .builder()
                .username(usuario.get().getNomeUsuario())
                .password(usuario.get().getSenha())
                .roles(roles)
                .build();
    }

    public Optional getUsuario(String nomeUsuario) {
        Optional<Administrador> administrador = getAdministrador(nomeUsuario);

        if(administrador.isPresent()) {
            return administrador;
        }

        return getEstudante(nomeUsuario);
    }

    private Optional<Administrador> getAdministrador(String nomeUsuario) {
        return administradorRepository.findByNomeUsuario(nomeUsuario);
    }

    private Optional<Estudante> getEstudante(String nomeUsuario) throws UsernameNotFoundException {
        return estudanteRepository.findByNomeUsuario(nomeUsuario);
    }

    public List<Administrador> getAdministradores() {
        return administradorRepository.findAll();
    }

    public List<Estudante> getEstudantes() {
        return estudanteRepository.findAll();
    }

    public void validarEExcluirUsuarioPeloId(Integer id) throws UsuarioNaoExisteException {
        Optional<Usuario> usuario = getUsuarioPeloId(id);

        if(usuario.isEmpty()) {
            throw new UsuarioNaoExisteException();
        }

        if(usuario.get() instanceof Administrador) {
            administradorRepository.delete((Administrador) usuario.get());
            return;
        }

        estudanteRepository.delete((Estudante) usuario.get());
    }

    public Optional getUsuarioPeloId(Integer id) {
        Optional<Administrador> administrador = administradorRepository.findById(id);

        if(administrador.isPresent()) {
            return administrador;
        }

        return estudanteRepository.findById(id);
    }
}
