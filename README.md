
(notesdump follows)

Path forward:

SETUP

1.  Parse documents as json or as text files.  grab each of them, give it an id, and stick it in database with entered = false.
2.  Parse questions in my own format.  construct maps for the frontend and for the database entry from questions.
3.  those two things need to be a cli, like BINARY - setup that also calls a database migration.  (maybe this can be in an ansible thing or something?)
4.  Assign coder passwords--- probably fine to just store them in clear as long as they're automatically assigned and unchangable (so that people can't reuse and suffer security problems on other sites). This could be part of setup too, just create a text file with a list of users, setup randomly assigns strings for their passwords, and then you just email the user and tell them their password, and it's their damn job to remember it.  (And then the randomly assigned strings are just saved to disk in addition to db, and the admin can just read them.  In the clear.  Honestly, if passwords can't be reused, why not just do it this way?) 


AFTER SETUP



LIVE VALIDATION
1.  All MC and t/f questions subject to live validation.  KISS: just wait till some reasonable N of documents is in (100?  500?) then fit a NB classifier to predict each question on each document.  If actual coding on a document differs from prediction, queue it up for re-entry by different research assistant.  if both research assistants agree, take that answer, if they disagree, queue it up one more time and take majority vote (perhaps also flag with an "ambiguous" flag?



Live validation as a property of questions (default no)
plug in a validator function 




# labelmaker

generated using Luminus version "2.9.11.70"

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run

## License

Copyright © 2017 FIXME




I think I wanna build a full epic text-data coding platform that takes a list of documents (as folder or json) and a list of questions and:

- displays the documents 
- asks questions of two kinds about the document: (a): general questions about whole document and specific info questions 
- for specific info questions asks user to highlight part of document that they got the answer from/their answer is about.  Then add that to database. 

This can just be a json config file that has question, answer types, possible answers, label, question type, etc.  

- also need a "preemptive type" that allows other data to just be ignored if you get a certain answer.  (Like "is this a contract.")

Also needs documents to start with metadata that gets put in db (source, etc.)

Then I can make available to others as a hosted webservice w/ db etc.  + free bec open source for self host 

And more importantly I can use myself---run a scraper to get a bunch of contracts by searching all the websites for "terms of service" etc., similar terms. 


Questions 
- question text (optional)
- question extra instructions (popup) (optional)
- question priority (optional, defaults to basic) (options basic and peremptory)
- question type (optional, defaults to general) general and highlight
- answer type (required, t/f, multiple choice, free answer, numeric)
- answer options (required, only if multiple choice)
- answer validation (optional, only if numeric, will need a dsl for this)


General Instructions (markdown) 


For multiple choice it might make sense to have one table for questions and mapping to answer choices, and another for the actual data, then output with a join 



If builds of PostgreSQL 9 are failing and you have version 8.x installed,
you may need to remove the previous version first. See:
  https://github.com/Homebrew/legacy-homebrew/issues/2510

To migrate existing data from a previous major version (pre-9.0) of PostgreSQL, see:
  https://www.postgresql.org/docs/9.6/static/upgrading.html

To migrate existing data from a previous minor version (9.0-9.5) of PostgreSQL, see:
  https://www.postgresql.org/docs/9.6/static/pgupgrade.html

  You will need your previous PostgreSQL installation from brew to perform `pg_upgrade`.
  Do not run `brew cleanup postgresql` until you have performed the migration.

To have launchd start postgresql now and restart at login:
  brew services start postgresql
Or, if you don't want/need a background service you can just run:
  pg_ctl -D /usr/local/var/postgres start
==> Summary
🍺  /usr/local/Cellar/postgresql/9.6.3: 3,259 files, 36.6MB
(py35)



very simple question syntax : 
blank line between question blocks.
each question header starts with a hash # 
for mc questions, each answer starts with a dash - 


CREATE TABLE users
(id VARCHAR(20) PRIMARY KEY,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 admin BOOLEAN,
 last_login TIME,
 is_active BOOLEAN,
 pass VARCHAR(300));
