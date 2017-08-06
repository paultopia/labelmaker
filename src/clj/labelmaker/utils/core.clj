(ns labelmaker.utils.core)

(defn veconcat [first & args]
  (into []
        (apply (partial concat first) args)))
