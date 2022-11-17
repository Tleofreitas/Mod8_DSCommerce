package com.devsuperiorT.dscommerce.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_category") // Nome da tabela no banco de dados
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id auto incrementável
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "categories")
    /* Em MUITOS para MUITOS, não pode repetir o par ProdutoId e CategoriaId,
     *  usa-se Set e Hash Set ao invés de List*/
    private Set<Product> products = new HashSet<>();

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }
}
