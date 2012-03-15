(ns ^{:doc "reference clojurescript one"}
  clj-rpc.net
  (:require [cljs.reader :as reader]
            [clojure.browser.net :as net]
            [clojure.browser.event :as event]
            [goog.net.XhrManager :as manager]
            [goog.net.EventType :as gevent-type]))

(let [request-ids (atom 0)]
  (defn request-id []
    (swap! request-ids inc)))

(extend-type goog.net.XhrManager
  
  event/EventType
  (event-types [this]
    (into {}
          (map
           (fn [[k v]]
             [(keyword (. k (toLowerCase)))
              v])
           (js->clj goog.net.EventType)))))

(defn- map->js [m]
  (let [out (js-obj)]
    (doseq [[k v] m]
      (aset out (name k) v))
    out))

(def ^:private *xhr-manager*
     (goog.net.XhrManager. nil
                           nil
                           nil
                           0
                           30000))

(defn success [e]
  (.log js/console "Complete : " e))

(defn error [e]
  (.log js/console "Error1 : " e))

(event/listen *xhr-manager* gevent-type/SUCCESS
              success)

(event/listen *xhr-manager* gevent-type/ERROR
              error)

(defn invoke-api
  [url str-func params]
  (.send *xhr-manager*
           (request-id)
           url
           "POST"
           (pr-str {:method str-func :params params})
           (map->js {"Content-type" "application/clojure;charset=utf-8" })
           nil
           nil
           0))

(.log js/console (invoke-api "http://localhost:9876/clj/invoke" "str" ["hello" "world"]))








