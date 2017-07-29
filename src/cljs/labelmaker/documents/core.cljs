(ns labelmaker.documents.core
  (:require [reagent.core :as r]
            [clojure.string :refer [blank?]]))

(defn get-last-real-selection [hatom]
  (last (remove blank? @hatom)))

(defn get-selected [hatom]
  (let [seltext (.toString (.getSelection js/window))]
    (swap! hatom conj seltext)
    (.log js/console (get-last-real-selection hatom))))

(defn highlightable-document [hatom content]
  [:div
   [:p {:on-mouse-up #(get-selected hatom)
        :on-touch-end #(get-selected hatom)} content]])

