(ns darkleaf.form.bootstrap4-test
  (:require [darkleaf.form.bootstrap4 :as sut]
            [cljs.test :as t :include-macros true]
            [reagent.core :as r]
            [darkleaf.form.context :as ctx]
            [goog.dom.classlist :as gclasslist]
            [clojure.string :as string]))

;; todo: remove
(enable-console-print!)

(defn with-rendered [element f]
  (let [container (.createElement js/document "div")
        component (r/render element container)
        react-root (.querySelector container "[data-reactroot]")]
    (f component react-root)
    (r/unmount-component-at-node container)
    (.remove container)))

(defn path-selector  [el path query]
  (let [path-query (str "[data-path='" path "']")
        result-query (str path-query " " query)]
    (.querySelector el result-query)))

;; рендерит корректно
;; рендерит правильное занчение
;; рендерит правильный тип
;; рендерит ошибки
;; при изменении значения меняет данные и перерендеривает

(t/deftest text
  (let [value "some value"
        data {:attr value}
        errors-ids [:error-1 :error-2]
        errors {:attr {ctx/errors-key errors-ids}}
        null-errors {}
        null-update (constantly nil)]
    (t/testing "render default"
      (let [f (ctx/build data null-errors null-update)]
        (with-rendered [sut/text f :attr]
          (fn [component root]
            (let [input (.querySelector root "input")]
              (t/is (= value (.-value input)))
              (t/is (= "text" (.-type input))))))))
    (t/testing "render with type"
      (let [f (ctx/build data null-errors null-update)]
        (with-rendered [sut/text f :attr :type :password]
          (fn [component root]
            (let [input (.querySelector root "input")]
              (t/is (= "password" (.-type input))))))))
    (t/testing "render with errors"
      (let [f (ctx/build data errors null-update)]
        (with-rendered [sut/text f :attr]
          (fn [component root]
            (t/is (gclasslist/contains root "has-danger"))
            (t/is (every? #(string/includes? (.-outerHTML root) %)
                          errors-ids))))))))

(comment
  (t/run-tests)
  #_:end-comment)

#_(let [data-atom (r/atom {:text/attribute "foo bar"})]

    (container-render
     [component]
     (fn [component container]
       (let [required-text (path-selector container [:text/attribute] "input")]
         (js/React.addons.TestUtils.Simulate.change
          required-text
          (clj->js {:target {:value ""}}))
         (r/flush)
         (js/console.log (.-innerHTML container))
         (prn @data-atom)))))
