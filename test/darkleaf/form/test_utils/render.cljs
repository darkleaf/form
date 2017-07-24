(ns darkleaf.form.test-utils.render
  (:require
   [reagent.core :as r]))

(declare ^{:dynamic true, :private true} *container*)

(def container-fixture
  {:before #(set! *container* (.createElement js/document "div"))
   :after  #(doto *container*
              (r/unmount-component-at-node)
              (.remove))})

(defn get-html []
  (.-outerHTML *container*))

(defn query-selector [query]
  (.querySelector *container* query))

(defn query-selector-all [query]
  (let [node-list (.querySelectorAll *container* query)
        js-array  (js/Array.prototype.slice.call node-list)]
    (js->clj js-array)))

(defn path-selector [path query]
  (let [path-query   (str "[data-path='" path "']")
        result-query (str path-query " " query)]
    (.querySelector *container* result-query)))

(defn render [el]
  (r/render el *container*))
