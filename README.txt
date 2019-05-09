use galaxia;

describe planetas;

create table galaxias (galaxiaId int auto_increment primary key, nombreGalaxia varchar(255), linkGalaxia varchar(255),usuarioId int, foreign key (usuarioId) references usuarios(usuarioId));
create table planetas ( planetaId int auto_increment primary key, nombrePlaneta varchar(255), edadPlaneta int, radioPlaneta double, galaxiaId int,linkPlaneta varchar(255), foreign key (galaxiaId) references galaxias(galaxiaId));
create table usuarios (usuarioId int auto_increment primary key,nombre varchar(255), password varchar(255),token varchar(255));

select *  from galaxias;
select * from planetas;
select *  from usuarios;

drop table galaxias;
drop table planetas;
drop table usuarios;
delete from galaxias where galaxiaId =2;
delete from usuarios where usuarioId =2;
select usuarioId from usuarios where token = 0OD1B7UNOCB2BZ0GL41THRQ8;