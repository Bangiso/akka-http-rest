CREATE database StudentDB;

use StudentDB;

CREATE TABLE students (
id INTEGER PRIMARY KEY,
name TEXT NOT NULL,
gpa double NOT NULL
);

INSERT INTO students (id, name, gpa) values
(1, 'Daniel', 89),
(2, 'Samuel', 76),
(3, 'Sanele', 96);