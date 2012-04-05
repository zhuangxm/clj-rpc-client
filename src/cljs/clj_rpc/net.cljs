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

(def ^:private *xhr-manager*
     (goog.net.XhrManager. nil
                           (.-strobj {"Content-type" "application/clojure;charset=utf-8" })
                           nil
                           0
                           30000))

(defn success?
  [{status :status}]
  (= status 200))

(defn create-invoke-request
  "create invoke request"
  [str-func params id]
  (let [id (or id str-func)]
    [str-func params id]))

(defn- gen-invoke-map
  "generate actual invoke requst map"
  [invoke-request]
  {:method (first invoke-request)
                  :params (nth invoke-request 1)
                  :id (nth invoke-request 2)})

(defn- handle-response
  [is-multi on-success on-error e]
  (let [response {:body   (. e/currentTarget (getResponseText))
                  :status (. e/currentTarget (getStatus))
                  :event  e}]
    (if (success? response)
      (let [data (reader/read-string (:body response))]
        (if is-multi 
          (doall (map on-success data))
          (on-success data)))
      (on-error response))))

(defn invoke-api
  "invoke remote clj-rpc service"
  [url on-success on-error invoke-request & other-requests]
  (let [invoke-requests (cons invoke-request other-requests)
        invoke-requests (map gen-invoke-map invoke-requests)]
    (.send *xhr-manager* (request-id) url "POST"
           (pr-str invoke-requests)
           nil nil (partial handle-response true on-success on-error)
           0)))

(defn get-api-help
  "get remote clj-rpc service help doc"
  [url on-success on-error]
  (.send *xhr-manager* (request-id) url "POST"
         nil nil nil (partial handle-response false on-success on-error)
         0))

(comment 
  (defn prn-result-log
    [data]
    (.log js/console data))

  (invoke-api "http://localhost:9876/clj/invoke"
              prn-result-log
              prn-result-log
              (create-invoke-request "str" ["hello" "world"]))

  (get-api-help "http://localhost:9876/clj/help"
                prn-result-log prn-result-log))








