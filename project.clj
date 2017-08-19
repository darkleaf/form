(defproject darkleaf/form "0.1.0"
  :description "Form builder for reagent library"
  :url "https://github.com/darkleaf/form"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.671" :scope "provided"]]
  :plugins [[lein-figwheel "0.5.11"]
            [lein-cljsbuild "1.1.6"]]
  :profiles {:dev {:dependencies [[reagent "0.7.0" :exclusions [cljsjs/react]]
                                  [cljsjs/react-with-addons "15.5.4-0"]
                                  [org.clojure/test.check "0.9.0"]
                                  [org.clojure/core.match "0.3.0-alpha4"]

                                  [figwheel-sidecar "0.5.8"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [karma-reporter "2.1.2"]]
                   :source-paths ["dev"]}}
  :clean-targets [:target-path "build"]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src" "test"]
                        :figwheel true
                        :compiler {:output-dir "build/dev"
                                   :output-to "build/dev/main.js"
                                   :main darkleaf.form-test.demo}}
                       {:id "gh-pages"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "gh-pages.js"
                                   :main darkleaf.form-test.demo
                                   :optimizations :advanced
                                   :parallel-build true}}
                       {:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-dir "build/test"
                                   :output-to "build/test/main.js"
                                   :main darkleaf.form-test.runner
                                   :asset-path "base"
                                   :optimizations :none
                                   :parallel-build true}}]})
