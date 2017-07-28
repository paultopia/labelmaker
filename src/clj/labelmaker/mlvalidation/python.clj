(ns labelmaker.mlvalidation.python
  "provides a framework to communicate with python.  Only 'public' function that is to be used in other namespaces is python-comlink! which should be called on startup, either via mount or main fn (after shell script starts rabbitmq and the python script---and postgre if it isn't running

  python-comlink! will take a callback function which will be the function that is applied to everything received from python.  It will return a sending function to be called to send something to python.

  This assumes there will be only one connection each direction between python and clojure, and all data will be in json.  Since literally all I want to do is pass data in and get weights back, that should be fine; if there's some kind of command or alternative thing then I can pass a map in with an instructions field and let python do what its told; ditto re: the other direction.

  Python will be expected to take data from the data queue and put it on the weights queue.  see snakepit repo for wiring on python side until implemented here."
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]
            [clojure.data.json :as json]
            [clojure.core.async
             :refer [>! <! >!! <!! go chan thread go-loop]]))


(defn say [outqueue async-channel message]
  (go
    (>! async-channel {:queue outqueue :content message})))

(defn dispatch-messages! [mq-channel async-channel]
  (go-loop []
    (let [{:keys [queue content]} (<! async-channel)]
      (lb/publish mq-channel "" queue (json/write-str content) {:content-type "application/json"}))
    (recur)))

(defn message-receiver
  [async-channel mq-channel metadata ^bytes payload]
  (let [message (json/read-str (String. payload "UTF-8"))]
    (go
      (>! async-channel message))))

(defn listen! [async-channel mq-channel queue]
  (let [handler (partial message-receiver async-channel)]
    (lc/subscribe mq-channel queue handler {:auto-ack true})))

(defn process-messages! [async-channel processor]
  (thread
    (loop []
      (let [message (<!! async-channel)]
        (processor message))
      (recur))))

(defn setup-async-comm [inqueue, outqueue]
  (let [mq-conn (rmq/connect)
        mq-channel (lch/open mq-conn)
        receiver-async-chan (chan)
        sender-async-chan (chan)
        sender-func (partial say outqueue sender-async-chan)
        receiver-func (partial process-messages! receiver-async-chan)]
    (dispatch-messages! mq-channel sender-async-chan)
    (lq/declare mq-channel inqueue {:exclusive false :auto-delete false})
    (lq/declare mq-channel outqueue {:exclusive false :auto-delete false})
    (listen! receiver-async-chan mq-channel inqueue)
    {:send! sender-func :receive! receiver-func}))

(defn python-comlink! [callback]
  (let [{:keys [send! receive!]} (setup-async-comm "weights" "data")]
    (receive! callback)
    send!))

