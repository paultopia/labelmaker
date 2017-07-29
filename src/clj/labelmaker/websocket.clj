(ns labelmaker.websocket
  (:require [immutant.web.async :as async]
            [clojure.data.json :as json]
            [clojure.core.async
             :refer [>! <! >!! <!! go chan thread go-loop buffer]]))

(defn to-json [data] (json/write-str data))
(defn from-json [data] (json/read-str data :key-fn keyword))

(def inchan (chan 100))

(defonce sockets (atom #{}))

(defn connect! [socket]
  (swap! sockets conj socket))

(defn disconnect! [socket {:keys [code reason]}]
  (swap! sockets #(remove #{socket} %)))

(defn take-incoming [this-socket msg]
  (let [message (from-json msg)]
    (go
      (>! inchan {:socket this-socket :message message}))))

(defn dispatch-outgoing [this-socket message]
  (doseq [socket @sockets]
    (async/send! socket (to-json {:word (:word message) :num (inc (:num message))}))))

(defn listen! [callback]
  (thread
    (loop []
      (let [{:keys [socket message]} (<!! inchan)]
        (callback socket message))
      (recur))))

(listen! dispatch-outgoing)

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open connect!
   :on-close disconnect!
   :on-message take-incoming})

(defn ws-handler [request]  
  (async/as-channel request websocket-callbacks))

