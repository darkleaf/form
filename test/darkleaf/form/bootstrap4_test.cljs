(ns darkleaf.form.bootstrap4-test
  (:require [darkleaf.form.bootstrap4 :as sut]
            [cljs.test :as t :include-macros true]
            [reagent.core :as r]
            [darkleaf.form.context :as ctx]
            [goog.dom.classlist :as gclasslist]
            [clojure.string :as string]))

(defn path-selector  [el path query]
  (let [path-query (str "[data-path='" path "']")
        result-query (str path-query " " query)]
    (.querySelector el result-query)))

(enable-console-print!)

(declare ^:dynamic *container*)

(def container-fixture
  {:before #(set! *container* (.createElement js/document "div"))
   :after #(doto *container*
             (r/unmount-component-at-node)
             (.remove))})

(t/use-fixtures :each container-fixture)

(def ^:private null-update (constantly nil))
(def ^:private null-errors {})

(defn test-plain-errors [element-builder data path-for-error]
  (let [cur-errors [:error-1 :error-2]
        errors (assoc-in {} path-for-error {ctx/errors-key cur-errors})
        f (ctx/build data errors null-update)
        el (element-builder f)
        _ (r/render el *container*)
        html (.-innerHTML *container*)]
    (t/is (every? #(string/includes? html %) cur-errors))))

(defn test-i18n-errors [element-builder data path-for-error]
  (let [cur-errors [:error-1 :error-2]
        errors (assoc-in {} path-for-error {ctx/errors-key cur-errors})
        i18n-text "some error"
        i18n-error (fn [path error]
                     (when (and
                            (= path path-for-error)
                            (= error (first cur-errors)))
                       i18n-text))
        f (ctx/build data errors null-update {:error i18n-error})
        el (element-builder f)
        _ (r/render el *container*)
        html (.-innerHTML *container*)]
    (t/is (string/includes? html i18n-text))))

(let [value "some value"
      data {:attr value}
      attr-path [:attr]]

  (t/deftest text-render
    (let [f (ctx/build data null-errors null-update)
          el [sut/text f :attr]
          _ (r/render el *container*)
          input (.querySelector *container* "input")]
      (t/is (= value (.-value input)))
      (t/is (= "text" (.-type input)))))

  (t/deftest text-render-with-type
    (let [f (ctx/build data null-errors null-update)
          el [sut/text f :attr :type :password]
          _ (r/render el *container*)
          input (.querySelector *container* "input")]
      (t/is (= "password" (.-type input)))))

  (t/deftest text-change
    (t/async
     done
     (let [new-value "new value"
           update (fn [path f]
                    (t/is (= attr-path path))
                    (t/is (= new-value (f :smth)))
                    (done))
           f (ctx/build data null-errors update)
           el [sut/text f :attr]
           _ (r/render el *container*)
           input (.querySelector *container* "input")]
       (js/React.addons.TestUtils.Simulate.change
        input
        (clj->js {:target {:value new-value}})))))

  (t/deftest text-plain-errors
    (test-plain-errors (fn [f] [sut/text f :attr])
                       data attr-path))

  (t/deftest text-i18n-errors
    (test-i18n-errors (fn [f] [sut/text f :attr])
                      data attr-path)))

(comment
  (t/run-tests))
