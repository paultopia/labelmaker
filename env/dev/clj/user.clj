(ns user
  (:require [mount.core :as mount]
            [labelmaker.figwheel :refer [start-fw stop-fw cljs]]
            labelmaker.core))

(defn start []
  (mount/start-without #'labelmaker.core/repl-server))

(defn stop []
  (mount/stop-except #'labelmaker.core/repl-server))

(defn restart []
  (stop)
  (start))


