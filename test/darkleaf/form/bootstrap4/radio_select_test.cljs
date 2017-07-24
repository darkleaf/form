(ns darkleaf.form.bootstrap4.radio-select-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.test-utils.render :as utils.render]
   [darkleaf.form.test-utils.events :as utils.events]
   [darkleaf.form.test-utils.common-checks :as utils.common-checks]
   [darkleaf.form.test-utils.blank :as utils.blank]))

(t/use-fixtures :each utils.render/container-fixture)

(def value "some value")
(def data {:some-attr value})
(def attr-path [:some-attr])
(def options [[value "Val 1"]
              ["2" "Val 2"]
              ["3" "Val 3"]])

(defn form-builder [f]
  [sut/radio-select f :some-attr :options options])

(t/deftest render
  (let [f     (ctx/build data utils.blank/errors utils.blank/update)
        el    (form-builder f)
        _     (utils.render/render el)
        inputs (utils.render/query-selector-all "input")]
    (t/is (=
           (count options)
           (count inputs)))))

(t/deftest change
  ;; There it uses synthetic events and doesn't rerender elements on change.
  ;; So I can trigger the change event on any radio inputs with any value.
  (utils.common-checks/usual-input-change
   form-builder data attr-path "input"))

(t/deftest plain-errors
  (utils.common-checks/plain-errors
   form-builder data attr-path))

(t/deftest i18n-errors
  (utils.common-checks/i18n-errors
   form-builder data attr-path))

(t/deftest plain-label
  (utils.common-checks/plain-label
   form-builder data "Some attr"))

(t/deftest i18n-label
  (utils.common-checks/i18n-label
   form-builder data attr-path))

(comment
  (t/run-tests))
