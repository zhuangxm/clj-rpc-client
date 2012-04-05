(defproject clj-rpc-client "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/clojurescript "0.0-993"]
                 [org.mozilla/rhino "1.7R3"]
                 [com.google.javascript/closure-compiler "r1810"]
                 [org.clojure/google-closure-library "0.0-790"]
                 [domina "1.0.0-alpha2"]
                 [clj-rpc "0.2.3"]]
  :repl-init clj-rpc.repl
  :source-path "src/clj"
  :hooks [leiningen.cljsbuild]
  :cljsbuild
  {:builds
   [{:source-path "src/cljs",
     :jar true
     :compiler
     {:output-to "public/js/main.js",
      :optimizations :whitespace,
      :pretty-print true}}]})
