(ns labelmaker.parsers.sql-questions)

;; this namespace needs to:
;; 1.  add each question to the questions table (incrementing qid each time)
;; 2.  store qid for each question in a map somewhere in state so that the data entry front-end can be constructed

;; I want a table just of questions
;; and a table of mc-answer options mapped to questions
;; and a table of responses

;; I'm going to have to manually increment the primary keys in order to make this work with 

(def question-mapping {:qid "INTEGER PRIMARY KEY"
                       :question "TEXT"})

(def mc-answer-opts-mapping {:aid "INTEGER PRIMARY KEY"
                             :qid "INTEGER"
                             :answeropt "TEXT"})


(defn response-mapping [respkey]
  {:id "SERIAL PRIMARY KEY"
   :qid "INTEGER"
   :answer (apicker respkey)})
