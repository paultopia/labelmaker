(ns labelmaker.db.management
  (:require [labelmaker.db.core :as db]
            [hugsql.core :as hsq]))

(defn empty-docs!! []
  (try (hsq/db-run db/*db* "DELETE FROM documents;")
       (catch Exception e)))

(defn add-stub-docs []
  (do
    (db/create-document! {:document "this is the first document"})
    (db/create-document! {:document "this is the second document"})))

(defn get-docs-queue []
  (db/get-coding-queue!))
