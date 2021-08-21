package com.andremachado.cursomc.dto;

import com.andremachado.cursomc.domain.Produto;

public class ProdutoDto {
	
	private Integer id;
	
	private String nome;
	
	private Double preco;
	
	public ProdutoDto() {
	}
	
	public ProdutoDto(Produto produto) {
		super();
		this.id = produto.getId();
		this.nome = produto.getNome();
		this.preco = produto.getPreco();
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}
}
