package com.deltavivo.literalura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="Livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private List<String> linguagem;
    private int donwloads;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores = new ArrayList<>();

    public Livro(DadosListaLivros dadosLivro){
        this.titulo = dadosLivro.livros().get(0).titulo();
        this.linguagem = dadosLivro.livros().get(0).linguagens();
        this.donwloads =  dadosLivro.livros().get(0).donwloads();
    }

    @Override
    public String toString() {
        return "Livro{" +
                ", titulo='" + titulo + '\'' +
                ", autores=" + autores +
                ", linguagens=" + linguagem +
                ", donwloads=" + donwloads +
                '}';
    }

    public void setAutores(List<Autor> autores) {
        autores.forEach(a -> a.setLivro(this));
        this.autores = autores;
    }

    public void setLinguagens(List<String> linguagens){

    }
}

