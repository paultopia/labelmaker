(ns labelmaker.state
  (:require [labelmaker.db.core :as db]))

;; currently untested.

(defonce coding-queue (atom []))
(defonce questions (atom []))
(defonce in-progress (atom {}))

(defn ensure-queue! []  ;; check to make sure there are more documents to code; handle fetching from db into state as needed; return true if there are more docs to code, false otherwise.
  (if (seq @coding-queue)
    true
    (let [dbdocs (db/get-coding-queue!)]
      (if (seq dbdocs)
        (do
          (reset! coding-queue dbdocs)
          true)
        false))))

(defn get-doc! []
  (if (ensure-queue!)
    (let [current-doc (peek @coding-queue)]
      (swap! coding-queue pop)
      (swap! in-progress assoc (:did current-doc) current-doc)
      current-doc)
    nil))

(defn get-questions! []
  (if (seq @questions)
    @questions
    (let [qs (db/get-codebook!)]
      (reset! questions qs)
      qs)))

(defn enter-coding!
"returns false and does not enter anything to database if record isn't in in-progress map.  this is meant to protect against multiple entry.  returns truthy value if record is in and entered successfully in both tables, otherwise returns false (and calling functions will have to figure out why?  Or should I throw an error here with more detail so that calling functions can recover?)

  this function could easily be buggy."
  [coding-response]
  (let [did (:did coding-response)
        document (get @in-progress did)]
    (if document
      (let [success? (db/add-response! coding-response)]
        (if success?
          (let [second-success? (db/update-document! (assoc coding-response :entered true))]
            (swap! in-progres dissoc did)
            second-success?)
          false))
      false)))
