package com.deltavivo.literalura.repository;

import com.deltavivo.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface
LivroRepository extends JpaRepository<Livro, UUID> {
    Optional<Livro>  findByTituloContainingIgnoreCase(String nomeLivro);
}
