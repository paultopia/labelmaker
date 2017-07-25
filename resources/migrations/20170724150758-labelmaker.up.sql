CREATE TYPE anstype as ENUM ('tf', 'multiplechoice', 'freeanswer', 'numerical');

CREATE TABLE questions
(qid SERIAL PRIMARY KEY,
 question TEXT,
 instructions TEXT,
 preemptive BOOLEAN,
 highlight BOOLEAN,
 answertype anstype,
 ansoptions TEXT);

CREATE TABLE documents
(did SERIAL PRIMARY KEY,
 document TEXT,
 entered BOOLEAN DEFAULT FALSE,
 flagged BOOLEAN DEFAULT FALSE,
 ambiguous BOOLEAN DEFAULT FALSE);

CREATE TABLE responses
(id SERIAL PRIMARY KEY,
 qid INTEGER,
 did INTEGER,
 userid VARCHAR(20),
 answer TEXT);

CREATE TABLE users
(username VARCHAR(20) PRIMARY KEY,
 password VARCHAR(20),
 isadmin BOOLEAN);
