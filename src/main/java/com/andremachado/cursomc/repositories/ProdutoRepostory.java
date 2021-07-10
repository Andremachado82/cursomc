package com.andremachado.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andremachado.cursomc.domain.Produto;

@Repository
public interface ProdutoRepostory extends JpaRepository<Produto, Integer> {

}
