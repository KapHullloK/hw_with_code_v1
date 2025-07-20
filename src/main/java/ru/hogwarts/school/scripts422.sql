create table car
(
    id bigserial primary key,
    mark  varchar(255),
    model varchar(255),
    cost  decimal(10, 2)
);


create table people
(
    id bigserial primary key,
    name varchar(255),
    age integer,
    is_driver_license boolean,
    car_id bigint references car(id)
);