(ns labelmaker.parsers.questions
  (:require [clj-yaml.core :as yaml]))

(defn parse-questions! []
  (-> "questions.yml" slurp yaml/parse-string))
