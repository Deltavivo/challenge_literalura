package com.deltavivo.literalura.repository;

import com.deltavivo.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface
LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro>  findByTituloContainingIgnoreCase(String nomeLivro);

    @Query("SELECT l FROM Livro l JOIN l.autores a WHERE a.nome ILIKE %:nome% ")
    List<Livro> findAllByAutor(String nome);

    @Query("SELECT l FROM Livro l ORDER BY l.donwloads DESC LIMIT 5 ")
    List<Livro> findTop5OrderByDonwloads();
}
