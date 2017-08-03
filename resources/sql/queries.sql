-- :name create-user! :! :n
-- :doc creates a new user
INSERT INTO users (username, password, isadmin)
VALUES (:username, :password, :isadmin)

-- :name change-password :! n
-- :doc changes user's password
UPDATE users
SET (password = :password)
WHERE username = :username

-- :name get-user :? :1
-- :doc retrieve a user given the username.
SELECT * FROM users
WHERE username = :username

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE username = :username

-- :name create-question! :! :n
-- :doc creates a new question
INSERT INTO questions (
question,
answertype
--~ (when (contains? params :peremptory) ",peremptory")
--~ (when (contains? params :instructions) ",instructions")
--~ (when (contains? params :highlight) ",highlight")
--~ (when (contains? params :answeroptions) ",answeroptions")
--~ (when (contains? params :validation) ",validation")
)
VALUES (
:question,
:answertype
--~ (when (contains? params :peremptory) ",:peremptory")
--~ (when (contains? params :instructions) ",:instructions")
--~ (when (contains? params :highlight) ",:highlight")
--~ (when (contains? params :answeroptions) ",:answeroptions")
--~
(when (contains? params :validation) ",:validation")
)

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
INSERT INTO responses (qid did userid answer highlight)
VALUES (:qid :did :userid :answer highlight)

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
