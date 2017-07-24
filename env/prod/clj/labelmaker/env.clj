(ns labelmaker.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[labelmaker started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[labelmaker has shut down successfully]=-"))
   :middleware identity})
