package com.andremachado.cursomc.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.andremachado.cursomc.domain.Cliente;

public class ClienteDto {
	
	private Integer id;
	
	@NotBlank(message="Nome deve ser preenchido")
	@Size(min=5, max=120, message="Tamanho deve ter entre 5 e 120 caracteres")
	private String nome;
	
	@NotBlank(message="Email deve ser preenchido")
	@Email(message="Email inválido")
	private String email;
	
	public ClienteDto() {}
	
	public ClienteDto(Cliente obj) {
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
