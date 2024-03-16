package com.andremachado.cursomc.dto;

import com.andremachado.cursomc.domain.Cidade;

public class CidadeDto {

    private Integer id;

    private String nome;

    public CidadeDto() {}

    public CidadeDto(Cidade obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
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
