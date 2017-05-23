(merge-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.542" :scope "provided"]
                 [hiccup "1.0.5" :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]])

(require 'cljs.repl.node
         'cemerick.piggieback)

(defn cljs-repl []
  (cemerick.piggieback/cljs-repl
   (cljs.repl.node/repl-env)
   :node-command "node_dom"))
