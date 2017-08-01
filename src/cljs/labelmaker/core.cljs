(ns labelmaker.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [labelmaker.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
            [labelmaker.ws :refer [websockets-comlink!]]
            [labelmaker.questions.core :refer [question-component]])
  (:import goog.History))

(defn just-log-it [foo]
  (.log js/console foo))

(def current-question (r/atom {:question "Test question here?"
                               :qid 0
                               :instructions nil
                               :peremptory nil
                               :highlight true
                               :answertype "multiplechoice"
                               :answeroptions (.stringify js/JSON (clj->js ["foo" "bar"]))
                               :validation nil}))

(def current-document (r/atom {:did 0 :document "Test document here!"}))

(def current-user (r/atom {:userid 0 :username "Test user."}))

(defn home-page []
  [:div.container
   [:div.row>div.col-sm-12
    [question-component current-question current-document current-user just-log-it]
    ]]) ;; when the time is right, I can swap out submission functions by replacing just-log-it with something that actually sends to server, processes, etc.


(defn log-messages [message]
  (.log js/console message))

(def send-ws! (websockets-comlink! log-messages)) ;sets up a sender + passes the logger to a listener.

(defn clickme []
  [:p
   [:button {:on-click #(send-ws! {:word "YO!!" :num 1})} "send 1"]])

(defn clickme2 []
  [:p
   [:button {:on-click #(send-ws! {:word "YO!!" :num 2})} "send 2"]])

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-dark.bg-primary
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "labelmaker"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Home" :home collapsed?]
         [nav-link "#/about" "About" :about collapsed?]]]])))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn documentation-page []
  [:div.container
   [:div.row>div.col-sm-12
    [clickme]
    [clickme2]]
   (when-let [docs (session/get :docs)]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(session/put! :docs %)}))

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
