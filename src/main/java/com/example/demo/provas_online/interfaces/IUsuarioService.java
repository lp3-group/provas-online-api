package com.example.demo.provas_online.interfaces;

import com.example.demo.provas_online.model.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface IUsuarioService {
    public Optional<Usuario> getUsuario(String nomeUsuario);
    public UserDetails loadUserByUsername(String username);
    public void salvar(Usuario usuario);
}
