(ns labelmaker.questions.core
  (:require [reagent.core :as r]
            [cljs.core.match :refer-macros [match]]))

;; answers are a map of: {:did :qid :answer :userid :done} where the last of these is to handle further-down UI for deciding whether to render the request a new document or not.

;; upstream I'll have these answers put on a core-async

;; questions should come in from server with {:question :qid :instructions :peremptory :highlight :answertype :answeroptions :validation}... remember that for mc I need to parse the answeroptions, which is a string.

;; answer options is changing to be json serialized now that I think of it. 

;; highlight will get handled in the document render function (which will have to rerender on every q to do so, but nbd.)

(defn instructions-component [instructions] ;; make me a popup later
  [:p instructions])

(defn submit-button-component [peremptory? answer]
  [:p
   [:button {:class "btn btn-success" :on-click #(.log js/console @answer)} "Yes."]])


(defn tf-component [answer]
  [:p
   [:button {:class "btn btn-primary" :on-click #(swap! answer assoc :answer true)} "Yes."]
   " "
   [:button {:class "btn btn-primary" :on-click #(swap! answer assoc :answer false)} "No."]])

(defn multiplechoice-component [{:keys [question answeroptions instructions]} answer]
  [:div])

(defn numeric-component [{:keys [question instructions]} answer]
  [:div])

(defn free-component [{:keys [question instructions]} answer]
  [:div])


(defn question-component [current-quatom current-docatom current-useratom]
  (let [{:keys [question answertype peremptory instructions qid] :as cq} @current-quatom
        userid (:userid @current-useratom)
        did (:did @current-docatom)
        answer (r/atom {:userid userid :did did :qid qid})]
    [:div
     [:p userid]
     [:p did]
     [:p question]
     (if instructions [instructions-component instructions] nil)
    (match answertype
           "tf" [tf-component answer]
           "multiplechoice" [multiplechoice-component cq answer]
           "numeric" [numeric-component cq answer]
           "free" [free-component cq answer])
     [submit-button-component peremptory answer]]))

