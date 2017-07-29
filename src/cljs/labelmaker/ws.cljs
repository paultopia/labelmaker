(ns labelmaker.ws
  "provides client-side functionality to send websockets data back and forth.

The only public function is websockets-comlink!, which when called from another namespace will take a callback with a function to handle incoming messages, and will return a function that can be used to send out outgoing messages

  (need to wire that up for json both ends)"
  (:import goog.net.WebSocket)
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [goog.events :as ev]
            [cljs.core.async :refer [>! <! chan buffer]]))

(def ws-uri
  (let [doc-uri (.-location js/window)]
    (str "ws://" (.-host doc-uri) (.-pathname doc-uri) "ws")))

;; architectural note: clients will send individual question answers back over websockets to be immediately entered, and when they've answered all questions, they'll send a "done" message with it.  Only on getting done message will server send another document.  server will also send ack messages, and if not received client will need to hold onto the data and try again.

(defn say [inchan message]
  (go
    (>! inchan message)))

(defn dispatch-messages! [socket outchan]
  (go-loop []
    (let [message (<! outchan)]
      (.log js/console "sending: " message)
      (.send socket message))
    (recur)))

(defn receive-messages [inchan callback]
  (go-loop []
    (let [message (<! inchan)]
      (callback message))
    (recur)))

(defn setup-websocket []
  (let [socket (WebSocket.)
        inchan (chan (buffer 10))
        outchan (chan (buffer 10))
        send (partial say outchan)
        receive (partial receive-messages inchan)]
    (.log js/console "WS URL is: " ws-uri)
    (dispatch-messages! socket outchan)
    (ev/listen socket
               #js [WebSocket.EventType.CLOSED
                    WebSocket.EventType.ERROR
                    WebSocket.EventType.OPENED]
               #(.log js/console (.-type %)))
    (ev/listen socket WebSocket.EventType.MESSAGE
               #(go
                   (>! inchan (.-message %))))
    (.open socket ws-uri)
    {:send! send :receive! receive}))

(defn websockets-comlink! [callback]
  (let [{:keys [send! receive!]} (setup-websocket)]
    (receive! callback)
    send!))
