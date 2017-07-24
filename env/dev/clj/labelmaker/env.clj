(ns labelmaker.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [labelmaker.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[labelmaker started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[labelmaker has shut down successfully]=-"))
   :middleware wrap-dev})
