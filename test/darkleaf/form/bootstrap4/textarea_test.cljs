(ns darkleaf.form.bootstrap4.textarea-test
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

(t/deftest textarea-render
  (let [f     (ctx/build data utils.blank/errors utils.blank/update)
        el    [sut/textarea f :some-attr]
        _     (utils.render/render el)
        input (utils.render/query-selector "textarea")]
    (t/is (= value (.-value input)))))

(t/deftest change
  (utils.common-checks/usual-input-change
   (fn [f] [sut/textarea f :some-attr])
   data attr-path "textarea"))

(t/deftest plain-errors
  (utils.common-checks/plain-errors
   (fn [f] [sut/textarea f :some-attr])
   data attr-path))

(t/deftest i18n-errors
  (utils.common-checks/i18n-errors
   (fn [f] [sut/textarea f :some-attr])
   data attr-path))

(t/deftest plain-label
  (utils.common-checks/plain-label
   (fn [f] [sut/textarea f :some-attr])
   data "Some attr"))

(t/deftest i18n-label
  (utils.common-checks/i18n-label
   (fn [f] [sut/textarea f :some-attr])
   data attr-path))

(comment
  (t/run-tests))
