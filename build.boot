(merge-env!
 :source-paths #{"src"}
 :resource-paths #{"html"}
 :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.671" :scope "provided"]

                 [reagent "0.6.2"]

                 [cider/cider-nrepl "0.15.0-SNAPSHOT" :scope "test"]
                 [refactor-nrepl "2.3.0-SNAPSHOT" :scope "test"]

                 [org.clojure/test.check "0.9.0" :scope "test"]

                 [pandeiro/boot-http "0.8.3" :scope "test"]
                 [adzerk/boot-cljs "1.7.228-2" :scope "test"]
                 [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                 [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                 [com.cemerick/piggieback "0.2.1" :scope "test"]
                 [weasel "0.7.0" :scope "test"]])

(require
 '[cider.tasks :refer [add-middleware]]
 '[pandeiro.boot-http :refer [serve]]
 '[adzerk.boot-cljs :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

(task-options! add-middleware
               {:middleware '[cider.nrepl/cider-middleware
                              refactor-nrepl.middleware/wrap-refactor]})

(deftask dev []
  (comp (serve :port 40002)
        (watch)
        (cljs-repl :port 40001
                   :ip "0.0.0.0"
                   :nrepl-opts {:port 40000
                                :bind "0.0.0.0"})
        (cljs :optimizations :none
              :compiler-options {:parallel-build true})))
