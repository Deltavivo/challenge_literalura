package com.deltavivo.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosListaLivros(
        @JsonAlias("count") int qtdLivros,
        @JsonAlias("results") List<DadosLivro> livros) {
}
