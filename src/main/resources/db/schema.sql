create table if not exists sites(
    id serial primary key,
    login text,
    password text,
    site text unique
);

create table if not exists links(
    id serial primary key,
    url text,
    site_id int references sites(id),
    total int default 0,
    code text unique
);