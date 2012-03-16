(ns web-command.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [clj-rpc.net :as net]
            [domina :as domina]
            [clojure.browser.repl :as repl]))

(def api-data (atom {}))

(defn fill-in-api [url data]
  (let [data (sort-by :name data)]
    (swap! api-data assoc url data)
    (let [p (domina/by-id "j_docList")]
      (doall
       (map #(domina/append! p (str "<li><a>" (:name %) "</a></li>")) data) ))))

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

