(ns labelmaker.test.db.core
  (:require [labelmaker.db.core :refer [*db*] :as db]
            [hugsql.core :as hsq]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [labelmaker.config :refer [env]]
            [mount.core :as mount]))

(defn empty-docs!! []
  (try (hsq/db-run db/*db* "DELETE FROM documents;")
       (catch Exception e
         (str "swallowed exception: " (.getMessage e)))))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'labelmaker.config/env
      #'labelmaker.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)
    (empty-docs!!)))

(deftest test-documents
  (is (= 1 (db/create-document!
             {:document "this is a document"})))

   (is (= "this is a document"
          (:document (first (db/get-coding-queue!))))))


