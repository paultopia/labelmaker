(ns labelmaker.documents.core
  (:require [reagent.core :as r]
            [clojure.string :refer [blank?]]))

(defn get-last-real-selection [hatom]
  (last (remove blank? @hatom)))

(defn get-selected [hatom visatom]
  (let [seltext (.toString (.getSelection js/window))]
    (swap! hatom conj seltext)
    (reset! visatom "block")))

;; one issue here is that this doesn't handle overdragging, only works if user lifts finger from mouse *within* the dom element of the document. 

(defn register-highlight! [text visatom answer]
  (do
    (swap! answer assoc :highlight text)
    (reset! visatom "none")))

(defn clear-highlight [visatom answer]
  (do
    (swap! answer assoc :highlight nil)
    (reset! visatom "none")))

;; obvs this will have to change to handle submission of the data to the server.

;; another problem is that right now even bare clicks within the space will trigger a re-highlight of the last thing.  I need to just allow one submission, and tear down the whole event listener afterward.  Which shouldn't actually be that hard, with react re-rendering components anyway?  maybe?  does react re-hang listeners when it re-renders??

(defn submission-button [hatom visatom answer]
  (let [text (get-last-real-selection hatom)]
    [:div {:class "alert alert-danger" :style {:display @visatom :position "fixed" :top "20%" :left "20%" :z-index "100000"}}
     [:p "You selected the following text. If that's correct, hit save below, otherwise hit clear.  (You can click the document again before submitting to change this.)"]
     [:p {:class "alert alert-warning"} text]
     [:button {:class "btn btn-primary" :on-click #(register-highlight! text visatom answer)} "Save highlight"]
     " "
     [:button {:class "btn btn-danger" :on-click #(clear-highlight visatom answer)} "Clear highlight"]]))

(defn highlightable-document [current-docatom answer]
  (let [hatom (r/atom [""])
        visatom (r/atom "none")
        content (:document @current-docatom)
        current-highlight (:highlight @answer)]
    [:div
     [:p
      [:strong "Highlight the portion of the document that corresponds to your answer."]]
     (if current-highlight
       [:p {:class "alert alert-info"} "Your current highlighted content is: "
        [:i current-highlight]]
       nil)
     [:p {:on-mouse-up #(get-selected hatom visatom)
          :on-touch-end #(get-selected hatom visatom)} content]
     [submission-button hatom visatom answer]]))


(defn plain-document [current-docatom answer]
  [:div
   [:p (:document @current-docatom)]])
