package com.deltavivo.literalura.principal;

import com.deltavivo.literalura.model.Autor;
import com.deltavivo.literalura.model.DadosListaLivros;
import com.deltavivo.literalura.model.DadosLivro;
import com.deltavivo.literalura.model.Livro;
import com.deltavivo.literalura.repository.AutorRepository;
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

    private final List<DadosLivro> dadosLivros = new ArrayList<>();

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
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
                    7 - Listar livro por idioma
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
                case 7:
                    listarLivrosPorIdioma();
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


        var verifica = livroRepository.findByTituloContainingIgnoreCase(nomeLivro);

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
                livroRepository.save(livro);

                System.out.println(dadosLivroEscolhido);
            }
        }
        return dadosLivroEscolhido;
    }

    private void buscarLivroPorAutor() {
        var autores = autorRepository.findAll();
        autores.forEach(System.out::println);

        System.out.print("Escolha qual autor quer procurar: \n");
        var nomeAutor = leitura.nextLine();

        var livros = livroRepository.findAllByAutor(nomeAutor);
        livros.forEach(System.out::println);

    }

    private void listarLivrosMaisBaixados() {
        List<Livro> livros = livroRepository.findTop5OrderByDonwloads();
        livros.forEach(System.out::println);
    }

    private void listarTodosOsLivros() {
        List<Livro> livros = livroRepository.findAll();

        if(livros.isEmpty()){
            System.out.print("Nenhum livro cadastrado.\n");
        }else {

            livros.stream()
                    .sorted(Comparator.comparing(Livro::getId))
                    .forEach(System.out::println);

        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAno() {

        System.out.print("Escolha qual ano deseja buscar autores vivos: \n");
        var ano = leitura.nextLine();
        List<Autor> autores = autorRepository.findByAuthorsAlive(Integer.getInteger(ano.trim()));

        if(!autores.isEmpty()) {
            autores.forEach(System.out::println);
        }else{
            System.out.printf("Nao existem livros com autores vivos.");
        }
    }

    private void listarLivrosPorIdioma(){
        System.out.print("Escolha o idioma para procurar (en,pt,br) : \n");
        var idioma = leitura.nextLine();

        List<String> i = new ArrayList<>();
        i.add(idioma);
        List<Livro> livros = livroRepository.findAllByLinguagem(i);
        livros.forEach(System.out::println);
    }


}
