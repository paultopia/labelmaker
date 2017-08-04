New executive decision: I'm going to accept that the open-source and easy deploy version of this is going to be insecure.  There is no way to just spin up easy security given the difficulty of getting into https and such; users will need to be told that if they want a secure version they should use something they pay for. 

the security goal for this will just be twofold:
1.  protecting against very weak attacks (i.e., there are actual passwords, even if they're transmitted in clear)
2.  protecting against weaknesses here leaking out (i.e., automatically assigned passwords so users can't reuse and get screwed on other places etc.)

another possibility is permitting heroku deploy only... it looks like foo.herokuapp.com already uses https by default? see https://devcenter.heroku.com/articles/ssl-endpoint -- and use heroku postgres?  that will increase the cost a bit though, essentially $7/month hobby dyno + $9/month postgres up to 10m rows (which seems a reasonable upper limit).  probably will lead to easier overall deploy though...

I think the authentication structure will just be based on two routes on serverside: root route will provide a login page at / and will be unauthenticated.  Then there will be an authenticated route that will actually have the reagent front-end.  and I can have a third, admin route down the road that will provide access both to admin tools and to the authenticated route.  but these choices will be on login page.

note: I need to make sure session tokens expire, and otherwise see recs here: http://www.lispcast.com/clojure-web-security

library for session timeouts: https://github.com/ring-clojure/ring-session-timeout 

cookie options: https://github.com/ring-clojure/ring/wiki/Cookies

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
- question text (mandatory)
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


PLANNING

executive decisions:
- documents are in markdown. 
- setup flow first
- first-pass admin interface is command line for setup. 
- questions are in yaml.
- buddy authentication because luminus already has it in.



rabbit holes
- pdfs
- admin interface
- Authoritative users who can trump validation process (like PI)?


setup flow: 
- user puts file of questions (yaml), a directory of documents (markdown), and a config file (yaml) in some appropriate location. 
    - config file gives paths to each
- user runs the app with a commandline flag that does setup.
- questions get parsed and added to database
- documents get parsed and added to database
- admin user(s) get set up with username and password and appropriate permissions for admin interface and put in database
- data entry user(s) get set up with username and password and put in database
    - each user gets assigned a password by person setting up.  
    - those passwords are hashed and put in database.

installation flow:
- jvm, postgres, get installed (ansible? docker?)
- database gets set up with appropriate username, permission, etc. (user-chosen.  shell script to set up the user and put in the config?) 
- maybe I don't want to give end-users direct database access at all?  just randomly pick a username and password and put it in the database on the fly?  Eh, that's no good, because if the app crashes that means bye bye data.  randomly pick and save somewhere but strongly discourage its use.

admin flow
- administrator logs in (or, first pass, enters commands at command line) (for command line interaction, no auth needed, rely on auth for the server?)
- can get answers to questions as csv joined with docs (database query)
- can add or remove users (database query)
- can view validation scores for users (database query)
- can get codebook from questions/instructions/questionformat/screenshot of interface?! (database query + some after the fact munging)
- can insert messages for some/all users (which are logged) (??? this might be too much for now.  basically for logging would be best to have a new database table, but, really?)  
- Can examine partial documents and decide what to do with them (send back to user/anyone, continue from left off/total recode, just finish coding self)
- this is pretty much all wrappers over database queries.  

user flow: 
- user logs in, is validated by server.  (login with google?  buddy?  friend?  chas emerick wrote friend, which is a pretty good recommendation)
- user says "I'm ready for a document", front-end sends request for document to server.
- server checks to see if document 
- server checks to see if there's an in-memory queue.  if not, it grabs 20 documents from the database and makes one
- server sends a document from the in-memory queue to user and stores the user and document in an in-progress list.
- server marks document as in-progress in the database (or in memory and does database later??) with init date and user.
- server sends document and first question
- user answers first question and (if question is of highlight type) (database needs field for that) highlights relevant part of document. 
- server sticks first answer in database (or batch adds?  down the road, think about duplicate entry protection and all that dba stuff), sends next question to user. 
- repeat last two steps until there are no more questions left.  (HOW TO TELL?  list of questions should actually be kept on front-end and it should just sequentially show them to users.  and that's actually easy implementation, each question-type has a view attached to it, the questions can be stored in a list of maps, and then can just go down the list displaying until there are no more.)
- server asks user if ready for another document.  goto 2. 

periodic verification flow
- every day, server checks to see if any users with pending in-progress documents have been pending for more than a day, and sends report for admin.
- every week, automatically carries out a default action (specified by setup) on pending in-progress docs where users on them haven't logged in for 24 hours (login condition to prevent edge case of killing a document when some user is working on it right then) 
- every hour, goes through and batch-marks documents in progress in memory to database?
- after some reasonable number of documents (500?) and every hundred documents thereafter, fits a model in python, and, if model is accurate enough, queues up some documents for recoding (make appropriate database changes +  (needs to also do a validation step at the end so the last (mod totaldocscount 100) docs don't go unvalidated)


numeric answer validation 
- greaterthan/lessthan
- odd/even
- integer?

