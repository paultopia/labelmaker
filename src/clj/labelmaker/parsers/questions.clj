(ns labelmaker.parsers.questions
  (:require [clojure.string :refer [split trim]]))


(defn parse-alist [alist]
  (let [answers (mapv trim (split alist #"-"))]
    (println (count answers))
    (vec (rest answers))))

(defn parse-block [hblock]
  (let [[header text] (split (trim hblock) #"\n" 2)
        item (if (= header "answeropts")
               (parse-alist text)
               text)]
    {(keyword header) item}))

(defn parse-1q [question]
  (let [hblocks (rest (split (trim question) #"#"))]  ; to get rid of leading blank line from earlier split.
    (apply merge (map parse-block hblocks))))

(defn parse-questions! []
  (let [qfile (trim (slurp "questions.txt"))
        questions (split qfile #"\n\n")
        qparts (mapv parse-1q questions)]
    (println qparts)
    ))
