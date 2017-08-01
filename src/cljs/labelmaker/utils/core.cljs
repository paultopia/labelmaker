(ns labelmaker.utils.core)

(defn to-json [data] (.stringify js/JSON (clj->js data)))
(defn from-json [data] (js->clj (.parse js/JSON data) :keywordize-keys true))
