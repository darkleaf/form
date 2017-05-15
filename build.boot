(merge-env!
 :source-paths #{"src"}
 :resource-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]
                 #_[org.clojure/clojurescript "1.9.542" :scope "provided"]

                 [org.clojure/clojurescript "1.9.493" :scope "provided"]

                 [cheshire "5.7.1" :scope "test"]

                 [degree9/boot-nodejs "1.0.0" :scope "test"]
                 [adzerk/boot-cljs "1.7.228-2" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]
                 [weasel "0.7.0" :scope "test"]])

(require '[adzerk.boot-cljs :refer [cljs]])
(require '[adzerk.boot-cljs-repl :refer [cljs-repl-env cljs-repl start-repl]])
(require '[degree9.boot-nodejs :refer :all])


(require 'boot.repl)
(swap! boot.repl/*default-middleware*
       conj 'cider.nrepl/cider-middleware)

(deftask dev []
  (comp
   (watch)
   (cljs-repl :port 50000
              :nrepl-opts {:port 40000
                           :bind "0.0.0.0"})
   (cljs :compiler-options {:target :nodejs})
   (target)
   #_(post-exec :process "node" :arguments ["main.js"])))


#_(replace-task!
   [t test] (fn [& xs]
              (merge-env! :source-paths #{"test/clj"})
              (apply t xs)))
