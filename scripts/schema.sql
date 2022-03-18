CREATE TYPE gender AS ENUM ('Male', 'Female');

CREATE TABLE clients
(
    id         UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    phone      VARCHAR(255),
    gender     gender,
    banned     BOOLEAN
);
