package com.deltavivo.literalura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name="autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String dataNascimento;

    private String dataFalecimento;

    @ManyToOne
    private Livro livro;

    public Autor(){}

    public Autor(String nome, String dataNascimento, String dataFalecimento){
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataFalecimento = dataFalecimento;
    }

    @Override
    public String toString() {
        return "Autor= " + nome +
                ", data nascimento= " + dataNascimento + '\'' +
                ", data falecimento= " + dataFalecimento;
    }
}
