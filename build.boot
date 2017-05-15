(merge-env!
 :resource-paths #{"src" "html"}
 ;;:source-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.542" :scope "provided"]

                 [pandeiro/boot-http          "0.7.1-SNAPSHOT" :scope "test"]
                 [adzerk/boot-cljs "2.0.0" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]
                 [weasel "0.7.0" :scope "test"]])

(require '[adzerk.boot-cljs :refer [cljs]])
(require '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])
(require '[pandeiro.boot-http    :refer [serve]])

(deftask dev []
  (comp (serve :port 3000)
        (watch)
        (cljs-repl :port 40001
                   :ip "0.0.0.0"
                   :nrepl-opts {:port 40000
                                :bind "0.0.0.0"})
        (cljs :source-map true :optimizations :none))

  #_(comp
     (watch)
     (cljs-repl :port 50000
                :nrepl-opts {:port 40000
                             :bind "0.0.0.0"})
     #_(cljs :compiler-options {:target :nodejs})
     (cljs)
     #_(target)))
