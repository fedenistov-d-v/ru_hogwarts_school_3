CREATE TABLE car(
    id uuid PRIMARY KEY ,
    brand VARCHAR NOT NULL ,
    model VARCHAR NOT NULL ,
    cost money
);
CREATE TABLE human(
    id uuid PRIMARY KEY ,
    surname VARCHAR NOT NULL ,
    name VARCHAR NOT NULL ,
    patronymic VARCHAR,
    age INTEGER CHECK ( age > 0 ),
    car_rights boolean NOT NULL DEFAULT FALSE,
    car_id uuid REFERENCES car(id)
);