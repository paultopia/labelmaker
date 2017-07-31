(ns labelmaker.parsers.core
  (:require [clj-yaml.core :as yaml]
            [labelmaker.db.core :as db]))

(defn load-yaml []
  (-> "config.yml" slurp yaml/parse-string))

(defn load-documents [dirname]
  (let [path (str "./" dirname)
        files (filter #(.isFile %) (file-seq (clojure.java.io/file path)))]
      (mapv slurp files)))

(defn load-all-into-memory! []
  (let [{:keys [questions docs]} (load-yaml)]
    {:questions questions :docs (load-documents docs)}))

;; after this works, I should actually make it load one document per time without holding them in memory, so I can handle document sets bigger than memory.

(defn load-all! []
  (let [{:keys [questions docs]} (load-yaml)
        documents (mapv #(identity {:document %}) (load-documents docs))]
    ;;(doall (map db/create-question! questions))
     (doall (map db/create-document! documents))
    ))

