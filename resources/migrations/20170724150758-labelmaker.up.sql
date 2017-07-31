CREATE TYPE anstype as ENUM ('tf', 'multiplechoice', 'freeanswer', 'numerical');

CREATE TABLE questions
(qid SERIAL PRIMARY KEY,
 question TEXT,
 instructions TEXT DEFAULT NULL,
 preemptive BOOLEAN DEFAULT FALSE,
 highlight BOOLEAN DEFAULT FALSE,
 answertype anstype,
 ansoptions TEXT DEFAULT NULL,
 validation TEXT DEFAULT NULL,
 dateadded TIMESTAMP DEFAULT CURRENT_TIMESTAMP);

CREATE TABLE documents
(did SERIAL PRIMARY KEY,
 document TEXT,
 entered BOOLEAN DEFAULT FALSE,
 flagged BOOLEAN DEFAULT FALSE,
 ambiguous BOOLEAN DEFAULT FALSE,
 dateadded TIMESTAMP DEFAULT CURRENT_TIMESTAMP);

CREATE TABLE responses
(id SERIAL PRIMARY KEY,
 qid INTEGER,
 did INTEGER,
 uid INTEGER,
 answer TEXT,
 dateadded TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 hasreentry BOOLEAN DEFAULT FALSE,
 isreentry BOOLEAN DEFAULT FALSE);

CREATE TABLE users
(uid SERIAL PRIMARY KEY
username TEXT,
 password TEXT,
 isadmin BOOLEAN,
 dateadded TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
