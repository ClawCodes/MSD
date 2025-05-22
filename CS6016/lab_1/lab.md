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

Salesman [__ssn (string)__, name (string)]

For_sale [__vin (string) [references Car.vin]__, __salesman_ssn (string)__ [references Salesman.vin], group_id (integer)]
```

# Part 2 - SQL Table Declarations

```sql
CREATE TABLE Titles
(
    ISBN (string) PRIMARY KEY,
    Title (string) NOT NULL,
    Author (string) NOT NULL,
    UNIQUE (Title, Author)
)

CREATE TABLE Patrons
(
    CardNum (integer) PRIMARY KEY,
    Name (string)
)

CREATE TABLE Inventory
(
    serial (integer) PRIMARY KEY,
    ISBN (string) FOREIGN KEY REFERENCES Titles(ISBN),
)

CREATE TABLE CheckedOut
(
    id (integer) PRIMARY_KEY,
    serial (integer) FOREIGN_KEY REFERENCES Inventory(serial),
    CardNum (integer) FOREIGN KEY REFERENCES Patrons(CardNum),
)

CREATE TABLE Phones
(
    id (integer) PRIMARY KEY,
    CardNum (integer) FOREIGN KEY REFERENCES Patrons(CardNum),
    Phone (string),
    UNIQUE (CardNum, Phone)
)
```

# Part 3 - Fill in Tables

Car

| __vin__  | make   | model   | year | color |
|----------|--------|---------|------|-------|
| 02458123 | Toyota | Tacoma  | 2008 | Red   |
| 92305402 | Toyota | Tacoma  | 1999 | Green |
| 02457234 | Tesla  | Model 3 | 2018 | White |
| 01467542 | Subaru | WRX     | 2016 | Blue  |
| 87541034 | Ford   | F150    | 2004 | Red   |

Salesman

| __ssn__  | name   | 
|----------|--------|
| 184-2749 | Arnold |
| 837-5926 | Hannah |
| 946-6389 | Steve  |

For_sale

| __vin__  | __salesman_ssn__ | group_id |
|----------|------------------|----------|
| 02458123 | 184-2749         | 1        |
| 92305402 | 184-2749         | 2        |
| 02458123 | 837-5926         | 1        |
| 87541034 | 837-5926         | 3        |
| 02457234 | 946-6389         | 4        |

# Part 4 - Keys and Superkeys

| Attribute Sets | Superkey? | Proper Subsets                                     | Key |
|----------------|-----------|----------------------------------------------------|-----|
| {A1}	          | No        | {}                                                 | No  |
| {A2}	          | No        | {}                                                 | No  |		
| {A3}	          | No        | {}                                                 | No  |		
| {A1, A2}	      | Yes       | {}, {A1}, {A2}                                     | Yes |		
| {A1, A3}	      | No        | {}, {A1}, {A3}                                     | No  |		
| {A2, A3}       | No        | {}, {A2}, {A3}                                     | No  |
| {A1, A2, A3}   | Yes       | {}, {A1, A2}, {A2, A3}, {A1, A3}, {A1}, {A2}, {A3} | No  |

# Part 5 - Abstract Reasoning

a. If {x} is a superkey, then any set containing x is also a superkey.
  * True - any set containing x guarantees uniqueness for a given row across each attribute as the values in x cannot be repeated.

b. If {x} is a key, then any set containing x is also a key.
  * False - If x is a key, then x is also a super key. Any set containing x would then violate the key rule that no proper subset of its fields is a superkey.

c. If {x} is a key, then {x} is also a superkey.
  * True - a definition of a key is that it must also be a superkey.

d. If {x, y, z} is a superkey, then one of {x}, {y}, or {z} must also be a superkey.
  * False - the complete set of attributes (x, y, and z) may be the least amount of attributes required to provide uniqueness across rows.

e. If an entire schema consists of the set {x, y, z}, and if none of the proper subsets of {x, y, z} are keys, then {x, y, z} must be a key.
  * False - if none of the proper subsets of {x, y, z} are keys, then we can assert that no individual attribute is a superkey. 
    This is because all keys must be superkeys, and it is impossible for single element sets to contain proper subsets which are superkeys, thus the key rule that single element sets violate is that they are superkeys.
    We can then assert that no two element set is a superkey as the prior statement confirms that none of its proper subsets are superkeys, thus the two element subsets must also violate the rule they are superkeys.
    Now that we confirmed that no proper subset of {x, y, z} are superkeys, then {x, y, z} cannot violate the rule that "no proper subset of its fields is a superkey".
    Thus for it to be a key, it must also be a superkey.
    However, there is no guarantee that it is a superkey, thus we cannot confidently state that {x, y, z} MUST be a key.
    
    
    