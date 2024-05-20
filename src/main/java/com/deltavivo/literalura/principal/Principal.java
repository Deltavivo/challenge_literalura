package com.deltavivo.literalura.principal;

import com.deltavivo.literalura.model.DadosListaLivros;
import com.deltavivo.literalura.model.DadosLivro;
import com.deltavivo.literalura.model.Livro;
import com.deltavivo.literalura.repository.LivroRepository;
import com.deltavivo.literalura.service.ConsumoApi;
import com.deltavivo.literalura.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String URL_BASE = "https://gutendex.com/books/";

    private List<Livro> livros = new ArrayList<>();

    private List<DadosLivro> dadosLivros = new ArrayList<>();

    private LivroRepository repository;

    public Principal(LivroRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Busca de livro por título
                    2 - Listagem de todos os livros
                    3 - Listar livros mais baixados
                    4 - Buscar livro por autor
                    5 - Listar autores
                    6 - Listar autores vivos por ano
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {

                case 1:
                    buscarLivroPorTitulo();
                    break;
                case 2:
                    listarTodosOsLivros();
                    break;
                case 3:
                    listarLivrosMaisBaixados();
                    break;
                case 4:
                    buscarLivroPorAutor();
                    break;
                case 5:
                    listarAutores();
                    break;
                case 6:
                    listarAutoresVivosPorAno();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivroPorTitulo() {

        DadosListaLivros dados = getDadosListaLivro();



    }


    private DadosListaLivros getDadosListaLivro() {
        System.out.println("Digite o nome do livro para busca");
        var nomeLivro = leitura.nextLine();
        DadosListaLivros dados = null;
        Livro livro = null;

        var verifica = repository.findByTituloContainingIgnoreCase(nomeLivro);

        if(verifica.isPresent()){
            System.out.printf("\nLivro ja cadastrada no banco!\n");
        } else {
            var json = consumo.obterDados(URL_BASE + "?search=" + nomeLivro.replace(" ", "+"));
            dados = conversor.obterDados(json, DadosListaLivros.class);

            if(dados != null){
                String jsonLista = null;

                if(dados.qtdLivros() > 1){
                    dados.livros().forEach(System.out::println);
                    System.out.println("Digite o codigo do livro para busca");
                    var codLivro = leitura.nextLine();
                    jsonLista = consumo.obterDados(URL_BASE + "?ids=" + codLivro);
                    dados = conversor.obterDados(json, DadosListaLivros.class);
                    livro = new Livro(dados);
                }

                repository.save(livro);
                System.out.println(dados);
            }
        }
        return dados;
    }

    private void buscarLivroPorAutor() {
    }

    private void listarLivrosMaisBaixados() {
    }

    private void listarTodosOsLivros() {
        livros = repository.findAll();

        livros.stream()
                .sorted(Comparator.comparing(Livro::getId))
                .forEach(System.out::println);

        dadosLivros.forEach(System.out::println);
    }

    private void listarAutores() {
    }

    private void listarAutoresVivosPorAno() {
    }


}
