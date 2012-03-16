(ns clj-rpc.repl
  (:require [clj-rpc.server :as server]))

;;It's strange to put require below ns, then the rhino repl will throw
;; NullPointException. Are there difference the require in the ns and
;; explicit call? 
(require '[cljs.repl :as repl])
(require '[cljs.repl.rhino :as rhino])
(require '[cljs.repl.browser :as browser])

(defn rhino-repl []
  (repl/repl (rhino/repl-env)))

(defn browser-repl []
  (repl/repl (browser/repl-env)) )

(defn test-server []
  (server/export-commands 'clojure.core nil)
  (server/start))

(println "type (rhino-repl) to start rhino cljs repl")
(println "type (browser-repl) to start browser cljs repl")
(println "type (test-server) to start clj-rpc test server")

