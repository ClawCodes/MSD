# Part 1 - English to Schema

a.

```    
Product [__sku (string)__, name (string)]

Inventory [product_sku (integer) [references Product.sku], quantity (integer), price (real)]
```

b.

```    
Product [__sku (string)__, name (string)]

Inventory [__isle (string)__, product_sku (integer) [references Product.sku], price (real)]
```
c.

```
Car [__vin (string)__, make (string), model (string), year (string), color (string)]

Salesman [__ssn (string)__, name (string), assigned_vin (string) [references Car.vin]]
```

# Part 2 - SQL Table Declarations

```sql
CREATE TABLE Titles (
    ISBN (string) PRIMARY KEY,
    Title (string) NOT NULL,
    Author (string) NOT NULL,
    UNIQUE (Title, Author)
)

CREATE TABLE Patrons (
    CardNum (integer) PRIMARY KEY,
    Name (string)
)

CREATE TABLE Inventory (
    serial (integer) PRIMARY KEY,
    ISBN (string) FOREIGN KEY REFERENCES Titles(ISBN),
)

CREATE TABLE CheckedOut (
    id (integer) PRIMARY_KEY,
    serial (integer) FOREIGN_KEY REFERENCES Inventory(),
    CardNum (integer) FOREIGN KEY REFERENCES Patrons(CardNum),
)

CREATE TABLE Phones (
    id (integer) PRIMARY KEY,
    CardNum (integer) FOREIGN KEY REFERENCES Patrons(CardNum),
    Phone (string),
    UNIQUE(CardNum, Phone)
)
```

