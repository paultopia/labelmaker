(ns labelmaker.utils.core)

(defn to-json [data] (.stringify js/JSON (clj->js data)))
(defn from-json [data] (js->clj (.parse js/JSON data) :keywordize-keys true))

(defn component-mapper
  [component-function root-component-keyword sequence]
  (reduce conj [root-component-keyword] (mapv component-function sequence)))
