(ns labelmaker.routes.home
  (:require [labelmaker.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [labelmaker.websocket :refer [websocket-handler! test-say-back]]
            [labelmaker.authentication :refer [check-user]]))

(defn home-page []
  (layout/render "home.html"))

(defn login-page []
  (layout/render "login.html"))

(defn secret-page []
  (layout/render "secret.html"))

(defn login-authenticate [request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])
        session (:session request)
        valid (check-user username password)]
    (if valid
      (let [next-url "/secret"  ;; this can change and dispatch based on form-params
            updated-session (assoc session :identity (keyword username) :admin (:isadmin valid))]
        (->
         (response/found next-url)  ;; response/found is a redirect.
         (assoc :session updated-session)))
      (layout/render "login.html"))))

(defn logout
  [request]
  (-> (response/found "/login")
      (assoc :session {})))

(defn gimme-request [request]
  (str request))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/login" []
       (login-page))
  (GET "/logout" [] logout)
  (POST "/login" [] login-authenticate)
  (GET "/ws" [] (websocket-handler! test-say-back))
  (GET "/debug" [] gimme-request)
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))

;; reminder for self for later: passing the function for the page in directly as in (GET "/" [] home-page) means the route will call that function with the request as an argument.  calling the function within the route as in   (GET "/" [] (home-page)) will call the function w/ no arguments.



(defroutes authenticated-routes
  (GET "/secret" []
       (secret-page)))

(defroutes admin-routes
  (GET "/admin" []
       (str "you're an admin!  congratulations!")))

;; so right now everything in experimental-secret-routes requires login to access.  (It's all in the handlers namespace.)  FURTHER DOWN THE LINE what I should do is: 1.  rename that.  2.  put everything except the login page in there.  3.  create a separate auth-pipeline with handler for admin (actually I should do that now), and then the login page will just be the only page people can see. 
