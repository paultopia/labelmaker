(ns labelmaker.parsers.sql-questions)

;; this namespace needs to:
;; 1.  add each question to the questions table (incrementing qid each time)
;; 2.  then just query the table to get the data I just stored, and stick it all in a map somewhere to send to the front end and build functions to send back in.  

;; I want a table just of questions
;; and a table of mc-answer options mapped to questions
;; and a table of responses

;; I'm going to have to manually increment the question primary keys in order to hold them in state I think.

;; also need a separate document parser that grabs every document, adds it to documents table, and marks it either as coded or not coded
