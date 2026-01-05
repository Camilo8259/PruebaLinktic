CREATE DATABASE IF NOT EXISTS comercio;
USE comercio;

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS inventarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL UNIQUE,
    cantidad INT NOT NULL
);

INSERT INTO productos (nombre, precio) VALUES ('Laptop Gamer', 1500.00);
INSERT INTO productos (nombre, precio) VALUES ('Mouse Inalambrico', 25.50);
INSERT INTO productos (nombre, precio) VALUES ('Teclado Mecanico', 80.00);

INSERT INTO inventarios (producto_id, cantidad) VALUES (1, 50);
INSERT INTO inventarios (producto_id, cantidad) VALUES (2, 200);
INSERT INTO inventarios (producto_id, cantidad) VALUES (3, 15);