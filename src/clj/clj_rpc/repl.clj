(ns clj-rpc.repl)

(require '[cljs.repl :as repl])
(require '[cljs.repl.rhino :as rhino])
(def env (rhino/repl-env))
(repl/repl env)
