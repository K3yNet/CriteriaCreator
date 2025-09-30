package com.example.criteria_project.repository;

import com.example.criteria_project.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {
    // A JpaSpecificationExecutor nos dá o método findAll(Specification, Pageable)
    // que precisamos para usar nosso CriteriaCreator.
}