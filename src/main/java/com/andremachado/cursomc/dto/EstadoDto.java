package com.andremachado.cursomc.dto;

import com.andremachado.cursomc.domain.Estado;

public class EstadoDto {

    private Integer id;

    private String nome;

    public EstadoDto() {}

    public EstadoDto(Estado estado) {
        this.id = estado.getId();
        this.nome = estado.getNome();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
