(ns labelmaker.parsers.users
  (:require [crypto.random :refer [url-part]]
            [clojure.string :refer [split starts-with?]]
            [buddy.hashers :as hashers]))

(defn adminator [line]
  (if (starts-with? line "*")
    {:isadmin true :username (subs line 1)}
    {:isadmin false :username line}))

(defn passwordinator [usermap]
  (let [clearpw (url-part 10)
        hashpw (hashers/derive clearpw)]
    {:plain (str (:username usermap) ": " clearpw (if (:isadmin usermap) " ADMINISTRATOR" ""))
     :encrypted (assoc usermap :password hashpw)}))

(defn parse-userfile []
  (let [uf (split (slurp "users.txt") #"\n")]
    (mapv (comp passwordinator adminator) uf)))


