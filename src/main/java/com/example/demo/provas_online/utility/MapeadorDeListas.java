package com.example.demo.provas_online.utility;

import org.modelmapper.ModelMapper;

import java.util.List;

public class MapeadorDeListas {
    public static <T, K> List<K> mapeiaListaParaListaDeDTO(List<T> listaOrigem, Class<K> classeDestino) {
        ModelMapper modelMapper = new ModelMapper();

        return listaOrigem.stream().map(item -> modelMapper.map(item, classeDestino)).toList();
    }
}
