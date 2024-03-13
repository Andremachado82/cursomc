package com.andremachado.cursomc.resources;

import java.net.URI;

import com.andremachado.cursomc.domain.Categoria;
import com.andremachado.cursomc.dto.CategoriaDto;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.andremachado.cursomc.domain.Pedido;
import com.andremachado.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService pedidoService;
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		
		Pedido pedido = pedidoService.findPedidoById(id);
			
		return ResponseEntity.ok().body(pedido);
	}

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid Pedido pedido) {
		pedido = pedidoService.insert(pedido);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(pedido.getId())
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	public ResponseEntity<Page<Pedido>> findPage(@RequestParam(defaultValue = "0") Integer page,
													   @RequestParam(defaultValue = "24") Integer linesPerPage,
													   @RequestParam(defaultValue = "instante") String orderBy,
													   @RequestParam(defaultValue = "DESC") String direction) {
		Page<Pedido> listPedidos = pedidoService.findPage(page, linesPerPage, orderBy, direction);

		return ResponseEntity.ok().body(listPedidos);
	}
}
