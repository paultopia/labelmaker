(ns labelmaker.state
  (:require [labelmaker.db.core :as db]))

(defonce coding-queue (atom []))
(defonce questions (atom []))

(defn ensure-queue! []  ;; check to make sure there are more documents to code; handle fetching from db into state as needed; return true if there are more docs to code, false otherwise.
  (if (seq @coding-queue)
    true
    (let [dbdocs (db/get-coding-queue!)]
      (if (seq dbdocs)
        (do
          (reset! coding-queue dbdocs)
          true)
        false))))
