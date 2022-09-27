DROP TABLE IF EXISTS superhero CASCADE;
DROP TABLE IF EXISTS organisation CASCADE;
DROP TABLE IF EXISTS super_organisation CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS sighting CASCADE;

create table superhero(
id SERIAL primary key,
name varchar(50) not null,
description varchar(250),
power varchar(50) not null
);

create table organisation(
id SERIAL primary key,
name varchar(50) not null,
address varchar(200) not null,
description varchar(250)
);

create table location(
id SERIAL primary key,
name varchar(50) not null,
address varchar(200) not null,
latitude int not null,
longitude int not null,
description varchar(250)
);

create table super_organisation(
superId int not null,
orgId int not null,
primary key(superId, orgId),
foreign key(superId) references superhero(id),
foreign key(orgId) references organisation(id)
);

create table sighting(
id SERIAL primary key,
superId int not null,
locationId int not null,
date date not null,
foreign key(superId) references superhero(id),
foreign key(locationId) references location(id)
);