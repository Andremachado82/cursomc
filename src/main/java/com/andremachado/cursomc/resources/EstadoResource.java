package com.andremachado.cursomc.resources;

import com.andremachado.cursomc.dto.CidadeDto;
import com.andremachado.cursomc.dto.EstadoDto;
import com.andremachado.cursomc.services.CidadeService;
import com.andremachado.cursomc.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
    @Autowired
    private EstadoService estadoService;

    @Autowired
    CidadeService cidadeService;

    @GetMapping
    public ResponseEntity<List<EstadoDto>> findAll() {
        List<EstadoDto> listaEstados = estadoService.findAll();

        return ResponseEntity.ok().body(listaEstados);
    }

    @RequestMapping(value = "/{idEstado}/cidades", method = RequestMethod.GET)
    public ResponseEntity<List<CidadeDto>>findAllCidades(@PathVariable Integer idEstado) {
        List<CidadeDto> cidades = cidadeService.findAllCidades(idEstado);
        return ResponseEntity.ok().body(cidades);
    }
}
