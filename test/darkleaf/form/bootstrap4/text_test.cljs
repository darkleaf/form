(ns darkleaf.form.bootstrap4.text-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.test-utils.render :as utils.render]
   [darkleaf.form.test-utils.common-checks :as utils.common-checks]
   [darkleaf.form.test-utils.blank :as utils.blank]))

(t/use-fixtures :each utils.render/container-fixture)

(def value "some value")
(def data {:some-attr value})
(def attr-path [:some-attr])

(t/deftest render
  (let [f     (ctx/build data utils.blank/errors utils.blank/update)
        el    [sut/text f :some-attr]
        _     (utils.render/render el)
        input (utils.render/query-selector "input")]
    (t/is (= value (.-value input)))
    (t/is (= "text" (.-type input)))))

(t/deftest render-with-type
  (let [f     (ctx/build data utils.blank/errors utils.blank/update)
        el    [sut/text f :some-attr :type :password]
        _     (utils.render/render el)
        input (utils.render/query-selector "input")]
    (t/is (= "password" (.-type input)))))

(t/deftest change
  (utils.common-checks/usual-input-change
   (fn [f] [sut/text f :some-attr])
   data attr-path "input"))

(t/deftest plain-errors
  (utils.common-checks/plain-errors
   (fn [f] [sut/text f :some-attr])
   data attr-path))

(t/deftest i18n-errors
  (utils.common-checks/i18n-errors
   (fn [f] [sut/text f :some-attr])
   data attr-path))

(t/deftest plain-label
  (utils.common-checks/plain-label
   (fn [f] [sut/text f :some-attr])
   data "Some attr"))

(t/deftest i18n-label
  (utils.common-checks/i18n-label
   (fn [f] [sut/text f :some-attr])
   data attr-path))

(comment
  (t/run-tests))
