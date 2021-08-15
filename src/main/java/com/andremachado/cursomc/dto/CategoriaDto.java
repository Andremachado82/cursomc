package com.andremachado.cursomc.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.andremachado.cursomc.domain.Categoria;

public class CategoriaDto {
	
	private Integer id;
	
	@NotBlank(message = "Nome não informado")
	@Size(min=5, max=80, message="Tamanho deve ter entre 5 e 80 caracteres")
	private String nome;
	
	public CategoriaDto() {}
	
	public CategoriaDto(Categoria categoria) {
		id = categoria.getId();
		nome = categoria.getNome();
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
