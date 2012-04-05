(ns clj-rpc.repl
  (:require [clj-rpc.server :as server]
            [cljs.repl :as repl]
            [cljs.repl.rhino :as rhino]
            [cljs.repl.browser :as browser]))

;;Attention
;;require cljs.repl.browser and cljs.repl.rhino
;;can not load simultaneously
;;the last require will take effect.
;;so in every function , we need to reload explicitly.

(defn rhino-repl []
  (require '[cljs.repl.rhino :reload true])
  (repl/repl (rhino/repl-env)))

(defn browser-repl []
  (require '[cljs.repl.browser :reload true])
  (repl/repl (browser/repl-env)) )

(defn test-server []
  (server/export-commands 'clojure.core nil)
  (server/start))

(println "type (rhino-repl) to start rhino cljs repl")
(println "type (browser-repl) to start browser cljs repl")
(println "type (test-server) to start clj-rpc test server")

