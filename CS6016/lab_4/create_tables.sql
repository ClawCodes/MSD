CREATE TABLE cars (
    vin VARCHAR(18) PRIMARY KEY,
    make VARCHAR(50),
    model VARCHAR(50),
    year YEAR,
    color VARCHAR(25)
);

CREATE TABLE salesmen (
    ssn VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE for_sale (
    vin VARCHAR(18) REFERENCES cars(vin),
    salesman_ssn VARCHAR(10) REFERENCES salesmen(ssn),
    group_id INTEGER UNSIGNED
);


INSERT INTO cars (vin, make, model, year, color)
    VALUES
    ('02458123', 'Toyota', 'Tacoma', '2008', 'Red'),
    ('92305402', 'Toyota', 'Tacoma', '1999', 'Green'),
    ('02457234', 'Tesla', 'Model 3', '2018', 'White'),
    ('01467542', 'Subaru', 'WRX', '2016', 'Blue'),
    ('87541034', 'Ford', 'F150', '2004', 'Red');

INSERT INTO salesmen (ssn, name)
VALUES
    ('184-2749', 'Arnold'),
    ('837-5926', 'Hannah'),
    ('946-6389', 'Steve');

INSERT INTO for_sale (vin, salesman_ssn, group_id)
VALUES
    ('02458123', '184-2749', 1),
    ('92305402', '184-2749', 2),
    ('02458123', '837-5926', 1),
    ('87541034', '837-5926', 3),
    ('02457234', '946-6389', 4)
