package com.andremachado.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.andremachado.cursomc.domain.Categoria;
import com.andremachado.cursomc.dto.CategoriaDto;
import com.andremachado.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;
	
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
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody @Valid CategoriaDto categoriaDto) {
		Categoria categoria = categoriaService.fromDto(categoriaDto);
		categoria = categoriaService.update(id, categoria);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(categoria.getId())
				.toUri();

		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Integer id) {
		categoriaService.deleleById(id);
		
		ResponseEntity.noContent().build();
	}
}
