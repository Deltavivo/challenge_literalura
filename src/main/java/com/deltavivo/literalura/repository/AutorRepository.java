package com.deltavivo.literalura.repository;

import com.deltavivo.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {

    @Query("SELECT a FROM Autor a WHERE a.nome = :nome")
    Optional<Autor> findByTitulo(String nome);

    @Query("SELECT a FROM Autor a WHERE a.dataFalecimento >= :ano")
    List<Autor> findByAuthorsAlive(int ano);
}
