package com.andremachado.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andremachado.cursomc.domain.Cidade;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{

    @Transactional(readOnly= true)
    @Query("SELECT c FROM Cidade c WHERE c.estado.id = :idEstado ORDER BY c.nome")
    List<Cidade> findCidades(@Param("idEstado") Integer idEstado);
}
