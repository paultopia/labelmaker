(ns labelmaker.authentication
  (:require [buddy.hashers :as hashers]
            [labelmaker.db.core :as db]))

(defn check-user [username password]
  (let [umap (db/get-user {:username username})
        valid? (hashers/check password (:password umap))]
    (if valid?
      {:username username :isadmin (:isadmin umap)}
      false)))
