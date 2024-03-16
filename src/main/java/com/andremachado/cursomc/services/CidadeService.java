package com.andremachado.cursomc.services;

import com.andremachado.cursomc.domain.Cidade;
import com.andremachado.cursomc.dto.CidadeDto;
import com.andremachado.cursomc.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;
    public List<CidadeDto> findAllCidades(Integer idEstado) {
        List<Cidade> cidades = cidadeRepository.findCidades(idEstado);
        return cidades
                .stream()
                .map(CidadeDto::new)
                .collect(Collectors.toList());
    }
}
