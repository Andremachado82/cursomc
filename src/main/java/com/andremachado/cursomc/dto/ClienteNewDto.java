package com.andremachado.cursomc.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.andremachado.cursomc.services.validation.ClienteInsert;

@ClienteInsert
public class ClienteNewDto {
	

	@NotBlank(message="Nome deve ser preenchido")
	@Size(min=5, max=120, message="Tamanho deve ter entre 5 e 120 caracteres")
	private String nome;
	
	@NotBlank(message="Email deve ser preenchido")
	@Email(message="Email inválido")
	private String email;
	
	@NotBlank(message="Cpf ou Cnpj deve ser preenchido")
	private String cpfOuCnpj;
	private Integer tipo;
	
	@NotBlank(message="Logradouro deve ser preenchido")
	private String logradouro;
	
	@NotBlank(message="Número deve ser preenchido")
	private String numero;
	
	private String complemento;
	
	@NotBlank(message="Bairro deve ser preenchido")
	private String bairro;
	
	@NotBlank(message="Cep deve ser preenchido")
	private String cep;
	
	@NotBlank(message="Telefone deve ser preenchido")
	private String telefone1;
	
	private String telefone2;
	private String telefone3;
	
	private Integer cidadeId;
	
	public ClienteNewDto() {
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public Integer getTipo() {
		return tipo;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public String getCep() {
		return cep;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public String getTelefone3() {
		return telefone3;
	}

	public Integer getCidadeId() {
		return cidadeId;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public void setTelefone3(String telefone3) {
		this.telefone3 = telefone3;
	}

	public void setCidadeId(Integer cidadeId) {
		this.cidadeId = cidadeId;
	}
}
