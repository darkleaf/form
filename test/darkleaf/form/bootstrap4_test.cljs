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

(defn change [input value]
  (js/React.addons.TestUtils.Simulate.change
   input
   (clj->js {:target {:value value}})))

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

(defn test-plain-label [element-builder data given-label]
  (let [f (ctx/build data null-errors null-update)
        el (element-builder f)
        _ (r/render el *container*)
        html (.-innerHTML *container*)]
    (t/is (string/includes? html given-label))))

(defn test-i18n-label [element-builder data path-for-label]
  (let [i18n-text "some label"
        i18n-label (fn [path]
                     (when (= path path-for-label)
                       i18n-text))
        f (ctx/build data null-errors null-update {:label i18n-label})
        el (element-builder f)
        _ (r/render el *container*)
        html (.-innerHTML *container*)]
    (t/is (string/includes? html i18n-text))))

(defn test-usual-input-change [element-builder data attr-path input-selector]
  (t/async
   done
   (let [new-value "new value"
         update (fn [path f]
                  (t/is (= attr-path path))
                  (t/is (= new-value (f :smth)))
                  (done))
         f (ctx/build data null-errors update)
         el (element-builder f)
         _ (r/render el *container*)
         input (path-selector *container* attr-path input-selector)]
     (change input new-value))))

;; ~~~~~~~~~~~~~~~~ text ~~~~~~~~~~~~~~~~
(let [value "some value"
      data {:some-attr value}
      attr-path [:some-attr]]

  (t/deftest text-render
    (let [f (ctx/build data null-errors null-update)
          el [sut/text f :some-attr]
          _ (r/render el *container*)
          input (.querySelector *container* "input")]
      (t/is (= value (.-value input)))
      (t/is (= "text" (.-type input)))))

  (t/deftest text-render-with-type
    (let [f (ctx/build data null-errors null-update)
          el [sut/text f :some-attr :type :password]
          _ (r/render el *container*)
          input (.querySelector *container* "input")]
      (t/is (= "password" (.-type input)))))

  (t/deftest text-change
    (test-usual-input-change (fn [f] [sut/text f :some-attr])
                             data attr-path "input"))

  (t/deftest text-plain-errors
    (test-plain-errors (fn [f] [sut/text f :some-attr])
                       data attr-path))

  (t/deftest text-i18n-errors
    (test-i18n-errors (fn [f] [sut/text f :some-attr])
                      data attr-path))

  (t/deftest text-plain-label
    (test-plain-label (fn [f] [sut/text f :some-attr])
                      data "Some attr"))

  (t/deftest text-i18n-label
    (test-i18n-label (fn [f] [sut/text f :some-attr])
                     data attr-path)))

;; ~~~~~~~~~~~~~~~~ textarea ~~~~~~~~~~~~~~~~
(let [value "some value"
      data {:some-attr value}
      attr-path [:some-attr]]

  (t/deftest textarea-render
    (let [f (ctx/build data null-errors null-update)
          el [sut/textarea f :some-attr]
          _ (r/render el *container*)
          input (.querySelector *container* "textarea")]
      (t/is (= value (.-value input)))))

  (t/deftest textarea-change
    (test-usual-input-change (fn [f] [sut/textarea f :some-attr])
                             data attr-path "textarea"))

  (t/deftest textarea-plain-errors
    (test-plain-errors (fn [f] [sut/textarea f :some-attr])
                       data attr-path))

  (t/deftest textarea-i18n-errors
    (test-i18n-errors (fn [f] [sut/textarea f :some-attr])
                      data attr-path))

  (t/deftest textarea-plain-label
    (test-plain-label (fn [f] [sut/textarea f :some-attr])
                      data "Some attr"))

  (t/deftest textarea-i18n-label
    (test-i18n-label (fn [f] [sut/textarea f :some-attr])
                     data attr-path)))

;; ~~~~~~~~~~~~~~~~ select ~~~~~~~~~~~~~~~~
(let [value "some value"
      data {:some-attr value}
      options [[value "Val 1"]
               ["2" "Val 2"]
               ["3" "Val 3"]]
      attr-path [:some-attr]]

  (t/deftest select-render
    (let [f (ctx/build data null-errors null-update)
          el [sut/select f :some-attr :options options]
          _ (r/render el *container*)
          input (.querySelector *container* "select")]
      (t/is (= value (.-value input)))))

  #_(t/deftest select-change
      (t/async
       done
       (let [new-value "asfd" #_(-> options second first)
             update (fn [path f]
                      (t/is (= attr-path path))
                      (t/is (= new-value (f :smth)))
                      (done))
             f (ctx/build data null-errors update)
             el [sut/select f :some-attr :options options]
             _ (r/render el *container*)
             input (.querySelector *container* "select")]
         (change input new-value)))))


(comment
  (t/run-tests))
