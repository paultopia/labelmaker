(ns labelmaker.parsers.core
  (:require [clj-yaml.core :as yaml]
            [labelmaker.db.core :as db]
            [labelmaker.utils.json :refer [to-json]]))

(defn load-yaml []
  (-> "config.yml" slurp yaml/parse-string))

(defn db-doc-from-file [filename]
  (let [docmap {:document (slurp filename)}]
    (db/create-document! docmap)))

(defn stream-documents-into-db! [dirname]
  (let [path (str "./" dirname)
        files (filter #(.isFile %) (file-seq (clojure.java.io/file path)))
        filenames (map #(str path "/" (.getName %)) files)]
    (run! db-doc-from-file filenames)))

(defn tidyopts [qmap]
  (let [aopts (:answeroptions qmap)] 
    (assoc qmap :answeroptions (to-json aopts))))

(defn load-all! []
  (let [{:keys [questions docs]} (load-yaml)
        qs (mapv tidyopts questions)]
    (run! db/create-question! qs)
     (stream-documents-into-db! docs)))

