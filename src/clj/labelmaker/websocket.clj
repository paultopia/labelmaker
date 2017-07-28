(ns labelmaker.websocket
  (:require [immutant.web.async :as async]))

(defonce channels (atom #{}))

(defn connect! [channel]
  (swap! channels conj channel))

(defn disconnect! [channel {:keys [code reason]}]
  (swap! channels #(remove #{channel} %)))

(defn notify-clients! [thischan msg]
  (do
    (spit "rec.txt" (str thischan "\n" msg))
    (doseq [channel @channels]
      (async/send! channel (str thischan " SENT " msg)))))

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open connect!
   :on-close disconnect!
   :on-message notify-clients!})

(defn ws-handler [request]  
  (async/as-channel request websocket-callbacks))

