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

(defn submit-highlight! [text visatom]
  (do
    (.log js/console text)
    (reset! visatom "none")))

(defn cancel-highlight [visatom]
  (do
    (.log js/console "CANCELLED!!")
    (reset! visatom "none")))

;; obvs this will have to change to handle submission of the data to the server.

;; another problem is that right now even bare clicks within the space will trigger a re-highlight of the last thing.  I need to just allow one submission, and tear down the whole event listener afterward.  Which shouldn't actually be that hard, with react re-rendering components anyway?  maybe?  does react re-hang listeners when it re-renders??

(defn submission-button [hatom visatom]
  (let [text (get-last-real-selection hatom)]
    [:div {:class "alert alert-danger" :style {:display @visatom :position "fixed" :top "20%" :left "20%" :z-index "100000"}}
     [:p "You selected the following text. If that's correct, hit submit below, otherwise hit cancel."]
     [:p {:class "alert alert-warning"} text]
     [:button {:class "btn btn-primary" :on-click #(submit-highlight! text visatom)} "submit"]
     " "
     [:button {:class "btn btn-danger" :on-click #(cancel-highlight visatom)} "cancel"]]))

(defn highlightable-document [content]
  (let [hatom (r/atom [""])
        visatom (r/atom "none")]
      [:div
       [:p {:on-mouse-up #(get-selected hatom visatom)
            :on-touch-end #(get-selected hatom visatom)} content]
       [submission-button hatom visatom]]))

