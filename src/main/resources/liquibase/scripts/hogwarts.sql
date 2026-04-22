-- liquibase formatted sql

-- changeset dfedenistov:1
CREATE INDEX student_name_index ON student (name);

-- changeset dfedenistov:2
CREATE INDEX faculty_nc_index ON faculty (name, color);