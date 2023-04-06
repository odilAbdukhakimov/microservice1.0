CREATE SCHEMA IF NOT EXISTS product;

create table if not exists product.product
(
    id serial primary key,
    name varchar,
    price double precision,
    description varchar,
    image_url varchar
);
