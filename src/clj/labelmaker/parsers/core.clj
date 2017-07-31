(ns labelmaker.parsers.core
  (:require [clj-yaml.core :as yaml]
            [labelmaker.db.core :as db]
            [clojure.string :refer [join]]))

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

(defn tidyopts [qmap]
  (let [aopts (:answeroptions qmap)] 
    (assoc qmap :answeroptions (join "," aopts))))

(defn load-all! []
  (let [{:keys [questions docs]} (load-yaml)
        qs (mapv tidyopts questions)
        documents (mapv #(identity {:document %}) (load-documents docs))]
    (doall (map db/create-question! qs))
     (doall (map db/create-document! documents))
    ))

