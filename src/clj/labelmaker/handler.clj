(ns labelmaker.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [labelmaker.layout :refer [error-page]]
            [labelmaker.routes.home :refer [home-routes authenticated-routes admin-routes]]
            [compojure.route :as route]
            [labelmaker.env :refer [defaults]]
            [mount.core :as mount]
            [labelmaker.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'authenticated-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats)
        (wrap-routes middleware/wrap-restricted))
    (-> #'admin-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats)
        (wrap-routes middleware/wrap-admin))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
