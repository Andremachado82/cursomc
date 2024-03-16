package com.andremachado.cursomc.services;

import com.andremachado.cursomc.domain.Estado;
import com.andremachado.cursomc.dto.EstadoDto;
import com.andremachado.cursomc.repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<EstadoDto> findAll() {
        List<Estado> listEstados = estadoRepository.findAllByOrderByNome();
        return listEstados
                .stream()
                .map(EstadoDto::new)
                .collect(Collectors.toList());
    }
}
