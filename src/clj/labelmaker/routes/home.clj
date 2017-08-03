(ns labelmaker.routes.home
  (:require [labelmaker.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [labelmaker.websocket :refer [websocket-handler! test-say-back]]))

(defn home-page []
  (layout/render "home.html"))

(defn secret-page []
  (str "secret secret!"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/ws" [] (websocket-handler! test-say-back))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))

(defroutes experimental-secret-routes
  (GET "/secret" []
       (secret-page)))
