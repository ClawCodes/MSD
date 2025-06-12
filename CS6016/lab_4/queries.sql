-- Part 3 - Simple Retrieval Queries

-- 1. Get the ISBNs of all books by <Author>
SELECT ISBN
FROM Titles
WHERE Author = 'Kennedy';
-- change author's name in quotes to filter

-- 2. Get Serial numbers (descending order) of all books by <ISBN>
SELECT Serial
FROM Inventory
WHERE ISBN = '978-0062278791' -- change ISBN in quotes to filter
ORDER BY Serial DESC;

-- 3. Get the Serial numbers and ISBNs of all books checked out by <Patron’s name>
SELECT ISBN,
       I.Serial
FROM Patrons AS p
         JOIN CheckedOut AS co
              ON p.CardNum = co.CardNum
         JOIN Inventory AS I
              ON I.Serial = co.Serial
WHERE p.Name = 'Joe';
-- Change name of person to filter

-- 4. Get phone number(s) and Name of anyone with <ISBN> checked out
SELECT Name,
       Phone
FROM Patrons AS p
         JOIN CheckedOut AS co
              ON p.CardNum = co.CardNum
         JOIN Inventory AS I
              ON I.Serial = co.Serial
         JOIN Phones AS phone
              ON phone.CardNum = p.CardNum
WHERE ISBN = '978-0062278791';
-- Change ISBN to filter

-- Part 4 - Intermediate Retrieval Queries

-- 1.
SELECT Author
FROM Inventory
         NATURAL JOIN Titles
ORDER BY Serial
LIMIT 5;
-- Change 5 to limit by N books

-- 2.
SELECT Name, Phone
FROM Patrons
         NATURAL JOIN (SELECT CardNum
                       FROM CheckedOut
                       ORDER BY Serial DESC
                       LIMIT 1) most_recent
         NATURAL JOIN Phones;

-- 3.
SELECT DISTINCT Name, Phone
FROM CheckedOut
         NATURAL JOIN Patrons
         NATURAL JOIN Phones;

-- 4.
SELECT DISTINCT Author, Title
FROM (SELECT *
      FROM Titles
               NATURAL LEFT JOIN Inventory) books
WHERE Serial IS NULL;

-- Part 5 - Chess Queries
-- 1.
SELECT Name, pID
FROM Players
ORDER BY Elo DESC
LIMIT 10;

-- 2.
SELECT DISTINCT Name, Elo
FROM Games AS g
         JOIN Players AS p
              ON g.BlackPlayer = p.pID;

-- 3.
SELECT DISTINCT Name
FROM Games AS g
         JOIN Players AS p
              ON g.WhitePlayer = p.pID
WHERE Result = 'W';

-- 4.
WITH filt_events AS (SELECT *
                     FROM Games as g
                              NATURAL JOIN Events as e
                     WHERE year(e.Date) >= 2014
                       AND year(e.Date) <= 2018
                       AND Site = 'Budapest HUN')
SELECT DISTINCT Name
FROM (SELECT p1.Name
      FROM Players as p1
               JOIN filt_events
                    ON p1.pID = filt_events.WhitePlayer
      UNION ALL
      SELECT p2.Name
      FROM Players as p2
               JOIN filt_events
                    ON p2.pID = filt_events.BlackPlayer) all_players;

-- 5.
WITH white_wins AS (SELECT eID
                    FROM Games
                             JOIN Players
                                  ON Games.WhitePlayer = Players.pID
                    WHERE Name = 'Kasparov, Garry'
                      AND Result = 'W'),
     black_wins AS (SELECT eID
                    FROM Games
                             JOIN Players
                                  ON Games.BlackPlayer = Players.pID
                    WHERE Name = 'Kasparov, Garry'
                      AND Result = 'B')
SELECT Site, Date
FROM (SELECT *
      FROM white_wins
      UNION
      SELECT *
      FROM black_wins) all_wins
NATURAL JOIN Events;

-- 6.
