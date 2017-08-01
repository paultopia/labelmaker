(ns labelmaker.utils.json
  (:require [clojure.data.json :as json]))

(defn to-json [data] (json/write-str data))
(defn from-json [data] (json/read-str data :key-fn keyword))
