package com.andremachado.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.andremachado.cursomc.domain.Categoria;
import com.andremachado.cursomc.dto.CategoriaDto;
import com.andremachado.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid CategoriaDto categoriaDto) {
		Categoria categoria = categoriaService.fromDto(categoriaDto);
		categoria = categoriaService.insert(categoria);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(categoria.getId())
				.toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		
		Categoria categoria = categoriaService.findCategoriaByPorId(id);
		
		return ResponseEntity.ok().body(categoria);
	}

	@GetMapping
	public ResponseEntity<List<CategoriaDto>> findAll() {

		List<Categoria> ListCategorias = categoriaService.findAll();
		
		List<CategoriaDto> listCategoriaDto = ListCategorias
				.stream()
				.map(categoria -> new CategoriaDto(categoria)) 
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(listCategoriaDto);
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<CategoriaDto>> findPage(@RequestParam(defaultValue = "0") Integer page, 	
													   @RequestParam(defaultValue = "24") Integer linesPerPage, 
													   @RequestParam(defaultValue = "nome") String orderBy, 
													   @RequestParam(defaultValue = "ASC") String direction) {
		Page<Categoria> ListCategorias = categoriaService.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDto> listCategoriaDto = ListCategorias
				.map(categoria -> new CategoriaDto(categoria));

		return ResponseEntity.ok().body(listCategoriaDto);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid CategoriaDto categoriaDto) {
		Categoria categoria = categoriaService.fromDto(categoriaDto);
		categoria.setId(id);
		categoria = categoriaService.update(categoria);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(categoria.getId())
				.toUri();

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Integer id) {
		categoriaService.deleleById(id);
		
		ResponseEntity.noContent().build();
	}
}
