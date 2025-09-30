package com.example.criteria_project.controller;

import com.example.criteria_project.filter.CriteriaCreator;
import com.example.criteria_project.model.Produto;
import com.example.criteria_project.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<Page<Produto>> buscarProdutos(
            @RequestParam MultiValueMap<String, String> params,
            Pageable pageable) {

        Specification<Produto> spec = CriteriaCreator.byFilterMap(params, Produto.class, null, null);

        Page<Produto> resultados = produtoRepository.findAll(spec, pageable);

        return ResponseEntity.ok(resultados);
    }
}