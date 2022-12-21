package com.example.demo.provas_online.api.controller;

import com.example.demo.provas_online.api.dto.AdministradorDTO;
import com.example.demo.provas_online.api.dto.CriarUsuarioDTO;
import com.example.demo.provas_online.api.dto.EstudanteDTO;
import com.example.demo.provas_online.api.dto.UsuariosDTO;
import com.example.demo.provas_online.exception.UsuarioJaExisteException;
import com.example.demo.provas_online.exception.UsuarioNaoExisteException;
import com.example.demo.provas_online.model.entity.Administrador;
import com.example.demo.provas_online.model.entity.Estudante;
import com.example.demo.provas_online.model.entity.Usuario;
import com.example.demo.provas_online.service.UsuarioService;
import com.example.demo.provas_online.types.TipoUsuario;
import com.example.demo.provas_online.utility.MapeadorDeListas;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    @Autowired
    private ModelMapper modelMapper;

    private final UsuarioService service;

    @PostMapping()
    public ResponseEntity criarUsuario(@RequestBody CriarUsuarioDTO requisicao) {
        try {
            Usuario usuario = modelMapper.map(requisicao, requisicao.getTipoUsuario() == TipoUsuario.administrador ? Administrador.class : Estudante.class);

            service.validarESalvarUsuario(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UsuarioJaExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping()
    public ResponseEntity obterUsuarios() {
        List<Administrador> administradores = service.getAdministradores();
        List<Estudante> estudantes = service.getEstudantes();

        UsuariosDTO retorno = new UsuariosDTO(
                MapeadorDeListas.mapeiaListaParaListaDeDTO(administradores, AdministradorDTO.class),
                MapeadorDeListas.mapeiaListaParaListaDeDTO(estudantes, EstudanteDTO.class)
        );

        return ResponseEntity.ok(retorno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluirUsuario(@PathVariable("id") Integer id) {
        try {
            service.validarEExcluirUsuarioPeloId(id);

            return ResponseEntity.noContent().build();
        } catch (UsuarioNaoExisteException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
