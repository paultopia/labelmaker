(ns labelmaker.websocket
  (:require [immutant.web.async :as async]
            [labelmaker.utils.json :refer [to-json from-json]]
            [clojure.core.async
             :refer [>! <! >!! <!! go chan thread go-loop buffer]]))

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

(defn listen! [callback]
  (thread
    (loop []
      (let [{:keys [socket message]} (<!! inchan)]
        (callback socket message))
      (recur))))

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open connect!
   :on-close disconnect!
   :on-message take-incoming})

(defn ws-handler [request]
  (async/as-channel request websocket-callbacks))

(defn test-say-back [this-socket message]
  (async/send! this-socket (to-json {:word (:word message) :num (inc (:num message))})))

(defn websocket-handler!
"the callback function will take the socket identifier that the message came from and the message.  that's the place where I'll figure out what type of message (with a multimethod?), shove data in db, and acknowledge success."
  [callback]
  (do
    (listen! callback)
    ws-handler))

