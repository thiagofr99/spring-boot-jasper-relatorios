package com.mballem.curso.jasper.spring.repository;

import com.mballem.curso.jasper.spring.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario,Long> {

    @Query( "SELECT f.id as id, f.nome as nome FROM Funcionario f WHERE f.nome LIKE %:nome% AND f.dataDemissao IS NOT NULL AND f.nivel.id = 1")
    List<Tuple> findFuncionariosByNome(String nome);


}
