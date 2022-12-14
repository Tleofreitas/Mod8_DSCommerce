package com.devsuperiorT.dscommerce.controllers;

import com.devsuperiorT.dscommerce.dto.ProductDTO;
import com.devsuperiorT.dscommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// Arquitetura por padrão de camadas
// Aqui é o controlador que chama o serviço
@RestController // Responder pela web
@RequestMapping(value = "/products") // Rota de mapeamento
public class ProductController {

	@Autowired // Dependencia | Chamando o serviço
	private ProductService service;

	// Teste com acesso ao banco de dados
	@GetMapping(value = "/{id}") // Retorno da consulta
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO dto = service.findById(id);
		return ResponseEntity.ok(dto); // Retorno Customizado
	}

	// ------- Buscar todos os produtos de forma paginada-------------
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(
			@RequestParam(name = "name", defaultValue = "") String name, Pageable pageable) {
		Page<ProductDTO> dto = service.findAll(name, pageable);
		return ResponseEntity.ok(dto); // Retorno Customizado
	}

	// ------- Adicionar um novo produto no Banco -------------
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
		dto = service.insert(dto);
		// Criar link para orecurso criado
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);// Retorno Customizado
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok(dto); // Retorno Customizado
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build(); // Retorno Customizado
	}
}
