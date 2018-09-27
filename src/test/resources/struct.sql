drop table if exists t_address_province;
create table t_address_province(
	id int auto_increment primary key,
	name varchar(20),
	code varchar(50),
	url varchar(100)
);

drop table if exists t_address_city;
create table t_address_city(
	id int auto_increment primary key,
	pId int,
	name varchar(20),
	code varchar(50),
	url varchar(100)
);

drop table if exists t_address_country;
create table t_address_country(
	id int auto_increment primary key,
	pId int,
	name varchar(20),
	code varchar(50),
	url varchar(100)
);


drop table if exists t_address_town;
create table t_address_town(
	id int auto_increment primary key,
	pId int,
	name varchar(50),
	code varchar(50),
	url varchar(100)
);


drop table if exists t_address_village;
create table t_address_village(
	id int auto_increment primary key,
	pId int,
	name varchar(50),
	code varchar(50),
	url varchar(100)
);


CREATE TABLE t_address_city_plus(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	cityId INT
);

INSERT INTO t_address_city_plus (cityId)
SELECT id 
FROM t_address_city WHERE NAME IN ('东莞市','中山市','儋州市');