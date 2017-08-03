(ns darkleaf.form-test.bootstrap4.add-nested-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [darkleaf.form.context :as ctx]
   [darkleaf.form-test.utils.render :as utils.render]
   [darkleaf.form-test.utils.blank :as utils.blank]
   [darkleaf.form-test.utils.common-checks :as utils.common-checks]))

(t/use-fixtures :each utils.render/container-fixture)

(defn new-item []
  {:name ""})

(defn form-builder [f]
  (let [items-f (ctx/nested f :items)]
    [sut/add-nested-btn items-f new-item :id "new-item"]))

(def data {:items []})

(t/deftest render
  (let [f  (ctx/build data utils.blank/errors utils.blank/update)
        el (form-builder f)
        _  (utils.render/render el)
        button (utils.render/query-selector "#new-item")]
    (t/is (some? button))))

(t/deftest click
  (utils.common-checks/change-data-by-click
   form-builder "#new-item"
   data
   {:items [(new-item)]}))

(comment
  (t/run-tests))
