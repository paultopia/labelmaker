(ns labelmaker.websocket
  (:require [immutant.web.async :as async]
            [clojure.data.json :as json]))

(defn to-json [data] (json/write-str data))
(defn from-json [data] (json/read-str data :key-fn keyword))


(defonce sockets (atom #{}))

(defn connect! [socket]
  (swap! sockets conj socket))

(defn disconnect! [socket {:keys [code reason]}]
  (swap! sockets #(remove #{socket} %)))

(defn notify-clients! [this-socket msg]
  (let [message (from-json msg)]
    (doseq [socket @sockets]
      (async/send! socket (to-json {:word (:word message) :num (inc (:num message))})))))

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open connect!
   :on-close disconnect!
   :on-message notify-clients!})

(defn ws-handler [request]  
  (async/as-channel request websocket-callbacks))

