(ns darkleaf.form.bootstrap4.checkbox-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.test-utils.render :as utils.render]
   [darkleaf.form.test-utils.common-checks :as utils.common-checks]
   [darkleaf.form.test-utils.blank :as utils.blank]))

(t/use-fixtures :each utils.render/container-fixture)

(def value true)
(def data {:some-attr value})
(def attr-path [:some-attr])

(defn form-builder [f]
  [sut/checkbox f :some-attr])

(t/deftest render
  (let [f     (ctx/build data utils.blank/errors utils.blank/update)
        el    (form-builder f)
        _     (utils.render/render el)
        input (utils.render/query-selector "input")]
    (t/is (= "checkbox" (.-type input)))))

(t/deftest change
  (utils.common-checks/checkbox-input-change
   form-builder data attr-path))

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
