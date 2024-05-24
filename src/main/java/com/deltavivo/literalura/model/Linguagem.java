package com.deltavivo.literalura.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class Linguagem {

    private String idioma;

    @ManyToMany(mappedBy = "linguagem")
    private Set<Livro> livros = new HashSet<>();

    public Linguagem(){}

    public Linguagem(String idioma){
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return  "\n, name=" + idioma + '\n' +
                ", books=" + livros;
    }

}
