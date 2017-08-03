(ns labelmaker.websocket
  (:require [immutant.web.async :as async]
            [labelmaker.utils.json :refer [to-json from-json]]
            [clojure.core.async
             :refer [>! <! >!! <!! go chan thread go-loop buffer]]))

(def inchan (chan 100))

(defonce sockets (atom #{}))

(defonce socket-user (atom {}))
;; if I've done this right, this should contain a mapping from sockets to authenticated users, s.t. I can associate websockets data with users without having it be supplied on clientside.

(defn connect! [identity socket]
  (do
    (swap! sockets conj socket)
    (swap! socket-user assoc (str socket) identity)))

(defn disconnect! [socket {:keys [code reason]}]
  (do
    (swap! sockets #(remove #{socket} %))
    (swap! socket-user dissoc (str socket))))

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

(defn websocket-callbacks [request]
  "WebSocket callback functions"
  {:on-open (partial connect! (get request :identity))
   :on-close disconnect!
   :on-message take-incoming})

(defn ws-handler [request]
    (async/as-channel request (websocket-callbacks request)))

(defn test-say-back [this-socket message]
  (let [username (get @socket-user (str this-socket))]
    (async/send! this-socket (to-json {:word (:word message)
                                       :num (inc (:num message))
                                       :username username}))))

(defn websocket-handler!
"the callback function will take the socket identifier that the message came from and the message.  that's the place where I'll figure out what type of message (with a multimethod?), shove data in db, and acknowledge success."
  [callback]
  (do
    (listen! callback)
    ws-handler))

