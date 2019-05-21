create database galaxia;
use galaxia;

create table usuarios (usuarioId int auto_increment primary key,nombre varchar(255), password varchar(255),token varchar(255));
create table galaxias (galaxiaId int auto_increment primary key, nombreGalaxia varchar(255), linkGalaxia varchar(255),usuarioId int, foreign key (usuarioId) references usuarios(usuarioId));
create table planetas ( planetaId int auto_increment primary key, nombrePlaneta varchar(255), edadPlaneta int, radioPlaneta double, galaxiaId int,linkPlaneta varchar(255), foreign key (galaxiaId) references galaxias(galaxiaId));
