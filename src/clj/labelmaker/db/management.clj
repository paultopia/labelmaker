(ns labelmaker.db.management
  (:require [labelmaker.db.core :as db]
            [hugsql.core :as hsq]))
;; functions to add docs, questions, users, etc. go here.
;; maybe separate namespace to get results and codebook out
;; and definitely separate namespace for data entry functions.



(defn get-coding-queue! []
  (db/get-coding-queue!))
