-- :name create-user! :! :n
-- :doc creates a new user
INSERT INTO users (username, password, isadmin)
VALUES (:username, :password, :isadmin)

-- :name create-users! :! :n
-- :doc creates multiple new users
INSERT INTO users (username, password, isadmin)
VALUES :tuple*users

-- :name get-user :? :1
-- :doc retrieve a user given the username.
SELECT * FROM users
WHERE username = :username

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id

-- :name create-question! :! :n
-- :doc creates a new question
INSERT INTO questions (question)
VALUES (:question)

-- :name create-document! :! :n
-- :doc creates a new document
INSERT INTO documents (document)
VALUES (:document)

-- :name update-document! :! :n
-- :doc update a document
UPDATE documents
SET (entered = :entered, flagged = :flagged, ambiguous = :ambiguous)
WHERE did = :did

-- :name add-response! :! :n
-- :doc adds a response
INSERT INTO responses (qid did userid answer)
VALUES (:qid :did :userid :answer)

-- :name get-full-responses! :? :*
-- :doc get all the data
SELECT *
FROM responses JOIN documents
ON did = did

-- :name get-codebook! :? :*
-- :doc get all the question info
SELECT *
FROM questions

-- :name get-coding-queue! :? :*
-- :doc get 20 docs to queue up for coding
SELECT * FROM documents
WHERE entered = false
ORDER BY did
LIMIT 20

-- to add: queries to get documents including flagged and unentered.
