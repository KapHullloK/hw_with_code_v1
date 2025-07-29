-- liquibase formatted sql

-- changeset afro_0:1
create index idx_name_student on student (name);

-- changeset afro_0:2
create index idx_name_and_color_faculty on faculty (name, color);