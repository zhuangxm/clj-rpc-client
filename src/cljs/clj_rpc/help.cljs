(ns web-command.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [clj-rpc.net :as net]
            [domina :as domina]
            [clojure.browser.repl :as repl]))

(def api-data (atom {}))

(defn fill-in-api [url data]
  (.log js/console "fill-in-api : " url)
  (swap! api-data assoc url data)
  (let [p (domina/by-id "j_docList")
        node (.createTextNode js/document "hello")]
    (.log js/console p "hello")
    (domina/append! p node)
    (doall
     (map (fn [d] (domina/append! p (.createTextNode js/document (get d :name)))) data) )))

(defn error [data]
  (js/alert data))

(defn get-api [url]
  (net/get-api-help url
                (partial fill-in-api url)
                error))

(defn start-app []
  (repl/connect "http://localhost:9000/repl")
  (get-api "http://localhost:9876/clj/help"))

(let [document (dom/getDocument)]
  (events/listen
   document
   goog.events.EventType/READYSTATECHANGE
   (fn []
     (when (= "complete" document/readyState)
       (start-app)))))

