package com.andremachado.cursomc.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andremachado.cursomc.domain.Cidade;
import com.andremachado.cursomc.domain.Cliente;
import com.andremachado.cursomc.domain.Endereco;
import com.andremachado.cursomc.domain.enums.TipoCliente;
import com.andremachado.cursomc.dto.ClienteDto;
import com.andremachado.cursomc.dto.ClienteNewDto;
import com.andremachado.cursomc.repositories.ClienteRepository;
import com.andremachado.cursomc.repositories.EnderecoRepository;
import com.andremachado.cursomc.services.exceptions.DataIntegrityException;
import com.andremachado.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	@Autowired
	BCryptPasswordEncoder bPasswordEncoder;
	
	public Cliente findClienteById(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}
	
	@Transactional
	public Cliente insert(Cliente cliente) {
		enderecoRepository.saveAll(cliente.getEnderecos());
		return clienteRepository.save(cliente);
	}
	
	public Cliente update(Cliente cliente) {
		Cliente newObj = findClienteById(cliente.getId());
		updateData(newObj, cliente);
//		BeanUtils.copyProperties(cliente, newObj, "cpfOuCnpj", "tipo");
		
		return clienteRepository.save(newObj);
	}

	public void deleleById(Integer id) {
		if (!clienteRepository.existsById(id)) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
		}
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui produtos");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pageRequest); 
	}
	
	public Cliente fromDto(ClienteDto clienteDto) {
		return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDto(ClienteNewDto clienteNewDto) {
		Cliente cliente = new Cliente(null, clienteNewDto.getNome(), clienteNewDto.getEmail(), 
									  clienteNewDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDto.getTipo()), 
									  bPasswordEncoder.encode(clienteNewDto.getSenha()));
		
		Cidade cidade = new Cidade(clienteNewDto.getCidadeId(), null, null);
		
		Endereco endereco = new Endereco(null, clienteNewDto.getLogradouro(), clienteNewDto.getNumero()
										, clienteNewDto.getComplemento(), clienteNewDto.getBairro(), clienteNewDto.getCep()
										 ,cliente, cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNewDto.getTelefone1());
		
		if (clienteNewDto.getTelefone2() != null) {
			cliente.getTelefones().add(clienteNewDto.getTelefone2());
		}
		
		if (clienteNewDto.getTelefone3() != null) {
			cliente.getTelefones().add(clienteNewDto.getTelefone3());
		}
		
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente cliente) {
		newObj.setNome(cliente.getNome());
		newObj.setEmail(cliente.getEmail());
	}
}
