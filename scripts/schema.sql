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

CREATE TABLE appointments
(
    id         UUID PRIMARY KEY,
    client_id  UUID REFERENCES clients (id),
    start_time TIMESTAMP WITH TIME ZONE,
    end_time   TIMESTAMP WITH TIME ZONE
);

CREATE TABLE services
(
    id             UUID PRIMARY KEY,
    appointment_id UUID REFERENCES appointments (id),
    name           VARCHAR(255),
    price          NUMERIC(10, 2),
    loyalty_points INTEGER
);

CREATE TABLE purchases
(
    id             UUID PRIMARY KEY,
    appointment_id UUID REFERENCES appointments (id),
    name           VARCHAR(255),
    price          NUMERIC(10, 2),
    loyalty_points INTEGER
);
