package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.LoginDTO;
import com.example.demo.provas_online.api.dto.LoginRetornoDTO;
import com.example.demo.provas_online.exception.SenhaInvalidaException;
import com.example.demo.provas_online.model.entity.Usuario;
import com.example.demo.provas_online.service.AuthService;
import com.example.demo.provas_online.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private ModelMapper modelMapper;

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity autenticar(@RequestParam String tipo, @RequestBody LoginDTO credenciais) {
        try {
            Usuario usuario = authService.autenticar(tipo, credenciais);
            String token = jwtService.gerarToken(usuario);

            LoginRetornoDTO retorno = modelMapper.map(usuario, LoginRetornoDTO.class);
            retorno.setToken(token);

            return ResponseEntity.ok(retorno);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
