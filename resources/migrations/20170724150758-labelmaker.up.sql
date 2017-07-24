CREATE TABLE questions
(qid SERIAL PRIMARY KEY,
 question TEXT);

CREATE TABLE documents
(did SERIAL PRIMARY KEY,
 document TEXT,
 entered BOOLEAN);

CREATE TABLE responses
(id SERIAL PRIMARY KEY,
 qid INTEGER,
 did INTEGER,
 answer TEXT);
