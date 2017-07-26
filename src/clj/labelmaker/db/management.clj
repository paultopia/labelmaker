(ns labelmaker.db.management
  (:require [labelmaker.db.core :as db]))

(defn add-stub-docs []
  (do
    (db/create-document! {:document "this is the first document"})
    (db/create-document! {:document "this is the second document"})))

(defn add-first-stub []
  (db/create-document! {:document "this is the first document"}))

(defn add-second-stub []
  (db/create-document! {:document "this is the second document"}))


(defn get-docs-queue []
  (db/get-coding-queue!))
