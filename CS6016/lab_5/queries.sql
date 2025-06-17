# 1
SELECT Name
FROM (SELECT Name,
             COUNT(*) AS num_checked_out
      FROM CheckedOut
               NATURAL JOIN Patrons
      GROUP BY CardNum) AS co_count
ORDER BY num_checked_out DESC
LIMIT 1;

# 2
SELECT Title
FROM Titles
WHERE Author LIKE 'K%';

# 3
SELECT Author
FROM Titles
GROUP BY Author
HAVING count(*) > 1;

# 4
SELECT Author
FROM Inventory
         NATURAL JOIN Titles
GROUP BY Author
HAVING count(*) > 1;

# 5
SELECT Name,
       num_checked_out,
       CASE
           WHEN num_checked_out > 2 THEN 'Platinum'
           WHEN num_checked_out = 2 THEN 'Gold'
           WHEN num_checked_out = 1 THEN 'Silver'
           ELSE 'Bronze'
           END AS loyalty_level
FROM Patrons
         NATURAL JOIN (SELECT CardNum,
                              count(*) num_checked_out
                       FROM CheckedOut
                                NATURAL JOIN Patrons
                       GROUP BY CardNum) co_count




