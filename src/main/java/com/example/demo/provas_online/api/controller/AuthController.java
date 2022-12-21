package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.LoginDTO;
import com.example.demo.provas_online.api.dto.LoginRetornoDTO;
import com.example.demo.provas_online.exception.SenhaInvalidaException;
import com.example.demo.provas_online.model.entity.Administrador;
import com.example.demo.provas_online.model.entity.Usuario;
import com.example.demo.provas_online.service.AuthService;
import com.example.demo.provas_online.service.JwtService;
import com.example.demo.provas_online.types.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private ModelMapper modelMapper;

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity autenticar(@RequestBody LoginDTO credenciais) {
        try {
            Usuario usuario = authService.autenticar(credenciais);
            String token = jwtService.gerarToken(usuario);

            LoginRetornoDTO retorno = modelMapper.map(usuario, LoginRetornoDTO.class);

            TipoUsuario tipo = usuario instanceof Administrador ? TipoUsuario.administrador : TipoUsuario.estudante;

            retorno.setTipoUsuario(tipo);
            retorno.setToken(token);

            return ResponseEntity.ok(retorno);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
