(ns labelmaker.questions.core
  (:require [reagent.core :as r]
            [labelmaker.utils.core :refer [to-json from-json component-mapper]]
            [labelmaker.documents.core :refer [highlightable-document plain-document]]
            [cljs.core.match :refer-macros [match]]))

;; answers are a map of: {:did :qid :answer :userid :done :highlight } where the last of these is to handle further-down UI for deciding whether to render the request a new document or not.

;; upstream I'll have these answers put on a core-async

;; questions should come in from server with {:question :qid :instructions :peremptory :highlight :answertype :answeroptions :validation}... remember that for mc I need to parse the answeroptions, which is a string.

;; answer options is changing to be json serialized now that I think of it. 

;; highlight will get handled in the document render function (which will have to rerender on every q to do so, but nbd.)

(defn instructions-component [instructions] ;; make me a popup later
  [:p instructions])

(defn submit-button-component [peremptory? answer submission-function]
  [:p
   [:button {:class "btn btn-success" :on-click #(submission-function @answer)} "Submit."]])  ;; put me on the async channel (or just a queue?!) to get processed later.  the submission-function parameter way up in what calls question-component will do the trick.



(defn tf-component [answer]
  (let [achoice (:answer @answer)]
    [:p
     [:button {:class (if achoice "btn btn-primary" "btn btn-secondary")
               :on-click #(swap! answer assoc :answer true)} "Yes."]
       " "
     [:button {:class (if (or achoice (nil? achoice)) "btn btn-secondary"  "btn btn-primary")
               :on-click #(swap! answer assoc :answer false)} "No."]]))

(defn mc-item [answer atext]
  (let [choice (:answer @answer)]
    [:p {:key atext}
     [:button {:class (if (= choice atext) "btn btn-primary" "btn btn-secondary")
               :on-click #(swap! answer assoc :answer atext)} "Select: "]
     " " atext]))

(defn multiplechoice-component [{aopts :answeroptions} answer]
  (let [choicelist (from-json aopts)]
    (component-mapper (partial mc-item answer) :div choicelist)))

(defn numeric-component [{:keys [question instructions]} answer]
  [:div])

(defn free-component [{:keys [question instructions]} answer]
  [:div])


(defn question-component [current-quatom
                          current-docatom
                          current-useratom
                          submission-function]
  (let [{:keys [question
                answertype
                peremptory
                instructions
                qid
                highlight] :as cq} @current-quatom
        userid (:userid @current-useratom)
        did (:did @current-docatom)
        answer (r/atom {:userid userid :did did :qid qid :answer nil :highlight nil})]
    [:div
     [:p question]
     (if instructions [instructions-component instructions] nil)
    (match answertype
           "tf" [tf-component answer]
           "multiplechoice" [multiplechoice-component cq answer]
           "numeric" [numeric-component cq answer]
           "free" [free-component cq answer])
     [submit-button-component peremptory answer submission-function]
     (if highlight
       [highlightable-document current-docatom answer]
       [plain-document current-docatom answer])]))

