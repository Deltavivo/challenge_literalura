create table livros(

    id bigint not null auto_increment,
    nome varchar(100) not null,
    dataNascimento DATE,
    dataFalecimento DATE,
    primary key(id)
);