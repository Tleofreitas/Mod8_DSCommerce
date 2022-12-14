package com.devsuperiorT.dscommerce.services;

import com.devsuperiorT.dscommerce.dto.ProductDTO;
import com.devsuperiorT.dscommerce.entities.Product;
import com.devsuperiorT.dscommerce.repositories.ProductRepository;
import com.devsuperiorT.dscommerce.services.exceptions.DatabaseException;
import com.devsuperiorT.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// Arquitetura por padrão de camadas
// Aqui é o serviço que chama o repository
@Service
public class ProductService {

	@Autowired // Dependencia | Chamando o repository
	private ProductRepository repository;

	/*
	 * @Transactional(readOnly=true) // Lock de leitura // Implementar busca no
	 * banco de dados public ProductDTO findById(Long id) { // Buscar no banco de
	 * dados o Id e atribuir na variável Optional<Product> result =
	 * Optional.of(repository.getReferenceById(id));
	 * 
	 * // Pegar o objeto Product dentro do Optional Product product = result.get();
	 * 
	 * // Converter o Product para ProductDTO ProductDTO dto = new
	 * ProductDTO(product);
	 * 
	 * // Retornar o DTO return dto; }
	 */
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = repository.findById(id).
		// Interceptar excessão e lançar excessão customizada
				orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new ProductDTO(product);
	}

	// ------- Buscar todos os produtos de forma páginada-------------
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(String name, Pageable pageable) {
		Page<Product> result = repository.searchByName(name, pageable);
		return result.map(x -> new ProductDTO(x));
	}

	// ------- Deletar um produto -------------
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");

		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	// ------- Adicionar um novo produto no Banco -------------
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		// Criar um produto
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		// Salvar o Produto no Repository
		entity = repository.save(entity);
		// Converter e retornar em DTO
		return new ProductDTO(entity);
	}

	// ------- Atualizar um produto no Banco -------------
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		// Tratamento para quando tentar atualizar um produto que não existe
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		// Passar o dto recebido para o produto
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
	}
}