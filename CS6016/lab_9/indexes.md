# Lab 9: Indexes

#### Part 1 - Selecting Indexes

* s1 - `Index(Start Date, End Date)` 
     - We can use a multicolumn index here because users
       want to filter the data by Start Date or Start Date AND End Date. There is no
       query in which a user only wants to filter by End Date, so there is no need for this
       column to have its own index.
* s2 - `Index(Grade)`
     - In both query scenarios, we only need to filter on the Grade column.
       Considering ASCII encoding, `WHERE Grade <= 'B'` will return all rows of interest for the first query
       and `WHERE Grade > 'D'` will return all rows of interest for the second query.
* s3 - `Index(className, Grade)`
     - This index supports "Get all classes ordered by class name" as an index on `className` will allow the
       DBMS to scan the index for the sorted order instead of sorting the column values itself. Additionally, the primary
       key index will not help here as `className` is the secondary column in the primary key's multicolumn index.
     - This also supports "Get all students who earned an 'A' in a certain class" we can first filter by a class of interest and then by the Grade = 'A'.
       while the index `Index(Grade, className)` would also support this case, this multi index order does not support the first query.
- s4 - `Index(Players.Elo)` and `Index(Games.WhitePlayer)`
     - `Index(Players.Elo)` supports the first query as the DBMS needs to scan the ELo column in players and this will speed up the search
       - `Index(Games.WhitePlayer)` supports the second query as Players already has an index for `pID`, thus does not require one, but Games
          does not have an index yet for `WhitePlayer`, the join in the second query will require scanning Games.WhitePlayer an an index will speed this operation up.
- s5 - None
     - Both the primary indexes on `Inventory` and `CheckedOut` are `Serial` which will be used in the natual join, no additional indexes would help in this case.
- s6 - `Index(CheckedOut.CardNum)`
     - This index supports the first query with faster filtering on `CardNum` (i.e. `CardNum=2`) it also supports a faster join to Patrons as the natural join would be on `CardNum`
- s7 - `Index(Inventory.ISBN)`
     - This LINQ query will result in each record of Titles being compared to the ISBN column in Inventory. Thus, an index on `Inventory.ISBN` will improve the performance of each scan on Inventory.ISBN.

#### Part 2: B+ Tree Index Structures

**Students:**

* How many rows of the table can be placed into the first leaf node of the primary index before it will split?
  * Assuming an insertion order in which the node's page storage reaches capacity then 273 rows can be captured in the leaf node.
    This is because each row will take 15 bytes, so 4096 // 15 = 273.
* What is the maximum number of keys stored in an internal node of the primary index?
  * The maximum number would be page size // studentID size = 4096 / 4 = 1,024
* What is the minimum number of rows in the table if the primary index has a height of 1?
  * This means we need the number of rows such that we have two leaf nodes. So, the leaf node is not the root and we have at least 1 additional row.
    Given our maximum page capacity if 273, we then need 274 rows to force a second leaf node.
* If there is a secondary index on Grade, what is the maximum number of entries a leaf node can hold in the secondary index?
  * 4069 // 1 = 4069

**Another table:**

* 47
* 2