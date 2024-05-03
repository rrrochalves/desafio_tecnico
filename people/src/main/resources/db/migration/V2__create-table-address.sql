CREATE TABLE address(
    id INTEGER NOT NULL PRIMARY KEY IDENTITY,
    logradouro VARCHAR(100),
    cep VARCHAR(8),
    numero INTEGER,
    cidade VARCHAR(30),
    estado VARCHAR(2),
    principal BOOLEAN,
    people_id INTEGER,
    CONSTRAINT address_people_id_fkey FOREIGN KEY (people_id) REFERENCES people (id) ON DELETE RESTRICT ON UPDATE CASCADE
);
