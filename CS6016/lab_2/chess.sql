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
    player_black integer,
    player_white integer,
    event_id integer,
    round integer,
    moves varchar(255),
    result varchar(8),
    UNIQUE (player_black, player_white, event_id, round), -- round is partial key, include for uniqueness in weak entity
    FOREIGN KEY (player_black, player_white) REFERENCES players (id, id),
    FOREIGN KEY (event_id) REFERENCES events (id)
)