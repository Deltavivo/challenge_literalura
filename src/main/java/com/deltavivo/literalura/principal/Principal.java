package com.deltavivo.literalura.principal;

import com.deltavivo.literalura.model.Autor;
import com.deltavivo.literalura.model.DadosListaLivros;
import com.deltavivo.literalura.model.DadosLivro;
import com.deltavivo.literalura.model.Livro;
import com.deltavivo.literalura.repository.LivroRepository;
import com.deltavivo.literalura.service.ConsumoApi;
import com.deltavivo.literalura.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

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
                    System.exit(0);
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivroPorTitulo() {

        DadosListaLivros dados = getDadosListaLivro();

        if(dados == null){
            System.out.printf("Livro nao encontrado.\n");
        }

    }


    private DadosListaLivros getDadosListaLivro() {
        System.out.println("Digite o nome do livro para busca");
        var nomeLivro = leitura.nextLine();
        DadosListaLivros dadosListaLivros = null;
        DadosListaLivros dadosLivroEscolhido = null;


        var verifica = repository.findByTituloContainingIgnoreCase(nomeLivro);

        if(verifica.isPresent()){
            System.out.printf("\nLivro ja cadastrada no banco!\n");
        } else {
            var jsonObterListaLivros = consumo.obterDados(URL_BASE + "?search=" + nomeLivro.replace(" ", "+"));
            dadosListaLivros = conversor.obterDados(jsonObterListaLivros, DadosListaLivros.class);

            if(dadosListaLivros.qtdLivros() > 0){

                dadosListaLivros.livros().forEach(System.out::println);
                System.out.println("Digite o codigo do livro para busca");
                var codLivro = leitura.nextLine();

                var jsonLivroEscolhido = consumo.obterDados(URL_BASE + "?ids=" + codLivro.trim());
                dadosLivroEscolhido = conversor.obterDados(jsonLivroEscolhido, DadosListaLivros.class);

                List<Autor> autores = dadosLivroEscolhido.livros().get(0).autores().stream()
                        .map(a-> new Autor( a.nome(), a.dataNascimento(), a.dataFalecimento()))
                                .collect(Collectors.toList());

                var l = dadosLivroEscolhido;
                l.livros().get(0).autores().remove(0);

                var livro = new Livro(l);
                livro.setAutores(autores);
                repository.save(livro);

                System.out.println(dadosLivroEscolhido);
            }
        }
        return dadosLivroEscolhido;
    }

    private void buscarLivroPorAutor() {
    }

    private void listarLivrosMaisBaixados() {
    }

    private void listarTodosOsLivros() {
        livros = repository.findAll();

        if(livros.isEmpty()){
            System.out.printf("Nenhum livro cadastrado.\n");
        }else {

            livros.stream()
                    .sorted(Comparator.comparing(Livro::getId))
                    .forEach(System.out::println);

            //2dadosLivros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
    }

    private void listarAutoresVivosPorAno() {
    }


}
