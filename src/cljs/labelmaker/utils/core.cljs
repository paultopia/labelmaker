(ns labelmaker.utils.core)

(defn to-json [data] (.stringify js/JSON (clj->js data)))
(defn from-json [data] (js->clj (.parse js/JSON data) :keywordize-keys true))

(defn component-mapper
  [component-function root-component-keyword sequence]
  (reduce conj [root-component-keyword] (mapv component-function sequence)))

(defn validate-numeric
  "tests s against numeric form (no commas allowed).  If forgiving, returns truthy value if s is nonexistent or if s consists only of numbers with or w/o decimal.  If strict, returns falsy value if s doesn't exist"
  ([s] (validate-numeric s :forgiving))
  ([s forgiveness]
   (if s
     (re-matches #"\d*\.?\d+" s)
     (if (= forgiveness :forgiving) true false))))
