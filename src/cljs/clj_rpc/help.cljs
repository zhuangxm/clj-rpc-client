(ns clj-rpc.help
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [clj-rpc.net :as net]
            [domina :as d]
            [domina.css :as css]
            [domina.events :as de]
            [clojure.browser.repl :as repl]
            [goog.string :as gstring]))

(defn box []
  (str "<box>"))

(def api-data (atom {}))

(defn all-data []
  (apply concat (vals @api-data) ))

(defn all-api-link []
  (css/sel "#j_docList > li"))

(defn find-doc [name]
  (first (filter #(= (:name %) name) (all-data))))

(defn fill-doc [name]
  (let [doc (find-doc name)]
    (d/set-text! (css/sel "#j_docContent > textarea") (get doc :doc))
    (d/set-text! (css/sel "#j_argList > p") (:arglists doc))
    (d/set-text! (css/sel "#j_options > p") (pr-str (:options doc)))))

(defn add-api-click-event []
  (let [nds (all-api-link)]
    (de/listen! nds
               "click"
               (fn [e]
                 (let [obj (.-currentTarget e)]
                   (d/remove-class! nds "current")
                   (d/add-class! obj "current")
                   (fill-doc (d/text (css/sel obj "a") )))))))

(defn fill-list [data include-str]
  (let [data (if (seq include-str)
               (filter #(let [_ (print " " (:name %) " " include-str)]
                          (gstring/contains (:name %)include-str)) data)
               data)
        p (d/by-id "j_docList")]
    (d/destroy-children! p)
    (doall
     (map #(d/append! p (str "<li id=><a href=\"javascript:;\">"
                             (:name %) "</a></li>")) data) )
    (add-api-click-event)))

(defn fill-in-api [url data]
  (let [data (sort-by :name data)]
    (swap! api-data assoc url data)
    (fill-list (all-data) "")))

(defn error [data]
  (js/alert data))

(defn get-api [url]
  (net/get-api-help url
                (partial fill-in-api url)
                error))

(defn add-search-event []
  (de/listen! (d/by-id "i_search")
              "keyup"
              (fn [e]
                (let [include-str (d/value (.-currentTarget e))]
                  (fill-list (all-data) include-str)))))

(defn start-app []
  (repl/connect "http://localhost:9000/repl")
  (get-api "http://localhost:9876/clj/help")
  (add-search-event))

(let [document (dom/getDocument)]
  (events/listen
   document
   goog.events.EventType/READYSTATECHANGE
   (fn []
     (when (= "complete" document/readyState)
       (start-app)))))
