(ns labelmaker.ws
  "provides client-side functionality to send websockets data back and forth.  When complete, will be structured like the mq is, that is, with a single function for use in other namespaces that takes a callback and returns a sender; the sender function will send to server and the callback will be called on anything received from the server."
  (:import goog.net.WebSocket)
  (:require [goog.events :as ev]))

(def ws-uri
  (let [doc-uri (.-location js/window)]
    (str "ws://" (.-host doc-uri) (.-pathname doc-uri) "ws")))

(defn setup-websocket []
  (let [socket (WebSocket.)]
    (.log js/console "WS URL is: " ws-uri)
    (ev/listen socket
               #js [WebSocket.EventType.CLOSED
                    WebSocket.EventType.ERROR
                    WebSocket.EventType.OPENED]
               #(.log js/console (.-type %)))
    (ev/listen socket WebSocket.EventType.MESSAGE  #(.log js/console (.-message %)))
    (.open socket ws-uri)
    socket))

(defn send-message [socket message]
  (do
    (.log js/console "sending a message")
    (.send socket message)))
