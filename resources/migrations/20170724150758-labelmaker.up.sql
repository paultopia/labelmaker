CREATE TABLE questions
(qid SERIAL PRIMARY KEY,
 question TEXT);

CREATE TABLE documents
(did SERIAL PRIMARY KEY,
 document TEXT,
 entered BOOLEAN,
 flagged BOOLEAN
 ambiguous BOOLEAN);

CREATE TABLE responses
(id SERIAL PRIMARY KEY,
 qid INTEGER,
 did INTEGER,
 userid VARCHAR(20),
 answer TEXT);

CREATE TABLE users
(username VARCHAR(20) PRIMARY KEY,
 password VARCHAR(20),
 admin BOOLEAN);
