CREATE TABLE departments
(
    name    varchar(101),
    subject varchar(5),
    PRIMARY KEY (subject)
);

CREATE TABLE administrators
(
    uid        varchar(9),
    first_name varchar(101),
    last_name  varchar(101),
    dob        date,
    PRIMARY KEY (uid)
);

CREATE TABLE students
(
    uid        varchar(9),
    first_name varchar(101),
    last_name  varchar(101),
    dob        date,
    major varchar(5),
    PRIMARY KEY (uid),
    FOREIGN KEY (major) REFERENCES departments (subject)
);

CREATE TABLE professors
(
    uid        varchar(9),
    first_name varchar(101),
    last_name  varchar(101),
    dob        date,
    department varchar(5),
    PRIMARY KEY (uid),
    FOREIGN KEY (department) REFERENCES departments (subject)
);

CREATE TABLE courses
(
    name            varchar(101),
    num             varchar(5),
    department varchar(101),
    id              serial,
    FOREIGN KEY (department) REFERENCES departments (subject),
    PRIMARY KEY (id),
    UNIQUE (department, num)
);

CREATE TABLE classes
(
    course_id bigint unsigned UNIQUE NOT NULL,
    semester  varchar(11),
    location  varchar(101),
    start     time,
    end       time,
    id        serial,
    professor varchar(9),
    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES courses (id),
    FOREIGN KEY (professor) REFERENCES professors (uid),
    UNIQUE (course_id, semester)
);

CREATE TABLE enrolled
(
    grade    varchar(3),
    uid      varchar(9),
    class_id bigint unsigned UNIQUE NOT NULL,
    FOREIGN KEY (uid) references students (uid),
    FOREIGN KEY (class_id) references classes (id),
    UNIQUE (uid, class_id)
);

CREATE TABLE assignment_categories
(
    name     varchar(101),
    weight   int unsigned,
    id       serial,
    class_id bigint unsigned UNIQUE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (class_id) references classes (id),
    UNIQUE (name, class_id),
    CHECK (weight BETWEEN 0 AND 100)
);

CREATE TABLE assignments
(
    name        varchar(101),
    points      int unsigned,
    content     text,
    due         datetime,
    category_id bigint unsigned UNIQUE NOT NULL,
    id          serial,
    FOREIGN KEY (category_id) REFERENCES assignment_categories (id),
    PRIMARY KEY (id),
    UNIQUE (name, category_id)
);

CREATE TABLE submissions
(
    dtm           datetime,
    score         int unsigned,
    content       text,
    assignment_id bigint unsigned UNIQUE NOT NULL,
    uid           varchar(9),
    FOREIGN KEY (assignment_id) REFERENCES assignments (id),
    FOREIGN KEY (uid) REFERENCES students (uid)
);
