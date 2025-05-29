CREATE TABLE players (
    id integer PRIMARY KEY,
    name varchar(50),
    elo_rating integer,
    UNIQUE (id, name, elo_rating) -- ensure unique with primary key as these are identifying natural keys
)

CREATE TABLE events (
    id integer PRIMARY KEY,
    name varchar(50),
    date date,
    site varchar(50),
    UNIQUE(id, name, date) -- ensure unique with primary key as these are identifying natural keys
)

CREATE TABLE games (
    player_black integer REFERENCES players (id),
    player_white integer REFERENCES players (id),
    event_id integer REFERENCES events (id),
    round integer,
    moves varchar(255),
    result varchar(8),
    UNIQUE (player_black, player_white, event_id, round)
)