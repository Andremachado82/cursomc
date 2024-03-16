package com.andremachado.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.andremachado.cursomc.domain.Categoria;
import com.andremachado.cursomc.domain.Cliente;
import com.andremachado.cursomc.dto.CategoriaDto;
import com.andremachado.cursomc.dto.ClienteDto;
import com.andremachado.cursomc.dto.ClienteNewDto;
import com.andremachado.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService clienteService;
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid ClienteNewDto clienteNewDto) {
		Cliente cliente = clienteService.fromDto(clienteNewDto);
		cliente = clienteService.insert(cliente);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(cliente.getId())
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> findById(@PathVariable Integer id) {
		Cliente cliente = clienteService.findClienteById(id);
		return ResponseEntity.ok().body(cliente);
	}

	@RequestMapping(value="/email", method = RequestMethod.GET)
	public ResponseEntity<Cliente> findByEmail(@RequestParam(value = "value") String email) {
		Cliente cliente = clienteService.findByEmail(email);
		return ResponseEntity.ok().body(cliente);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<ClienteDto>> findAll() {

		List<Cliente> ListClientes = clienteService.findAll();
		
		List<ClienteDto> listClienteDto = ListClientes
				.stream()
				.map(cliente -> new ClienteDto(cliente)) 
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(listClienteDto);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/page")
	public ResponseEntity<Page<ClienteDto>> findPage(@RequestParam(defaultValue = "0") Integer page, 	
													   @RequestParam(defaultValue = "24") Integer linesPerPage, 
													   @RequestParam(defaultValue = "nome") String orderBy, 
													   @RequestParam(defaultValue = "ASC") String direction) {
		Page<Cliente> ListClientes = clienteService.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDto> listClienteDto = ListClientes
				.map(cliente -> new ClienteDto(cliente));

		return ResponseEntity.ok().body(listClienteDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid ClienteDto clienteDto) {
		Cliente cliente = clienteService.fromDto(clienteDto);	
		cliente.setId(id);
		cliente = clienteService.update(cliente);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(cliente.getId())
				.toUri();

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Integer id) {
		clienteService.deleleById(id);
		
		ResponseEntity.noContent().build();
	}

	@RequestMapping(value="/picture", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfile(@RequestParam(name = "file") MultipartFile file) {
		URI uri = clienteService.uploadProfileImage(file);
		return ResponseEntity.created(uri).build();
	}
}
