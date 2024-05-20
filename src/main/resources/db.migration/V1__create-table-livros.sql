create table livros(

    id bigint not null auto_increment,
    titulo varchar(200) not null,
    linguagem varchar(100),
    downloads long not null,
    primary key(id)
);