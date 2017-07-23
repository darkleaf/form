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

;; (t/deftest change
;;   (utils.common-checks/usual-input-change
;;    (fn [f] [sut/select f :some-attr :options options])
;;    data attr-path "select"))

(t/deftest plain-errors
  (utils.common-checks/plain-errors
   (fn [f] [sut/radio-select f :some-attr :options options])
   data attr-path))

(t/deftest i18n-errors
  (utils.common-checks/i18n-errors
   (fn [f] [sut/radio-select f :some-attr :options options])
   data attr-path))

(t/deftest plain-label
  (utils.common-checks/plain-label
   (fn [f] [sut/radio-select f :some-attr :options options])
   data "Some attr"))

(t/deftest i18n-label
  (utils.common-checks/i18n-label
   (fn [f] [sut/radio-select f :some-attr :options options])
   data attr-path))

(comment
  (t/run-tests))
