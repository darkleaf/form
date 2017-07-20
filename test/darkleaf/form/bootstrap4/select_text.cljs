(ns darkleaf.form.bootstrap4.select-text
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

(t/deftest render
  (let [f     (ctx/build data utils.blank/errors utils.blank/update)
        el    [sut/select f :some-attr :options options]
        _     (utils.render/render el)
        input (utils.render/query-selector "select")]
    (t/is (= value (.-value input)))))

#_(t/deftest change
    (t/async
     done
     (let [new-value "asfd" #_(-> options second first)
           update (fn [path f]
                    (t/is (= attr-path path))
                    (t/is (= new-value (f :smth)))
                    (done))
           f (ctx/build data null-errors update)
           el [sut/select f :some-attr :options options]
           _ (utils.render/render el)
           input (utils.render/query-selector "select")]
       (utils.events/change input new-value))))

(t/deftest plain-errors
  (utils.common-checks/plain-errors
   (fn [f] [sut/select f :some-attr :options options])
   data attr-path))

(t/deftest i18n-errors
  (utils.common-checks/i18n-errors
   (fn [f] [sut/select f :some-attr :options options])
   data attr-path))

(t/deftest plain-label
  (utils.common-checks/plain-label
   (fn [f] [sut/select f :some-attr :options options])
   data "Some attr"))

(t/deftest i18n-label
  (utils.common-checks/i18n-label
   (fn [f] [sut/select f :some-attr :options options])
   data attr-path))

(comment
  (t/run-tests))
