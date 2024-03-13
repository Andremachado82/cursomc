package com.andremachado.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andremachado.cursomc.domain.Produto;
import com.andremachado.cursomc.dto.ProdutoDto;
import com.andremachado.cursomc.resources.utils.URL;
import com.andremachado.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService produtoService;
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		
		Produto produto = produtoService.findProdutoById(id);
			
		return ResponseEntity.ok().body(produto);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProdutoDto>> findPage(@RequestParam(defaultValue = "") String nome,
													 @RequestParam(defaultValue = "") String categorias,
													 @RequestParam(defaultValue = "0") Integer page, 	
													 @RequestParam(defaultValue = "24") Integer linesPerPage, 
													 @RequestParam(defaultValue = "nome") String orderBy, 
													 @RequestParam(defaultValue = "ASC") String direction) {
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> pageProduto = produtoService.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		Page<ProdutoDto> listProdutoDto = pageProduto.map(ProdutoDto::new);

		return ResponseEntity.ok().body(listProdutoDto);
	}
	
}
