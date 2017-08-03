(ns darkleaf.form-test.bootstrap4.nested-map-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [darkleaf.form.context :as ctx]
   [darkleaf.form-test.utils.render :as utils.render]
   [darkleaf.form-test.utils.blank :as utils.blank]))

(t/use-fixtures :each utils.render/container-fixture)

(defn form-builder [f]
  [:div
   (for [[key item-f] f]
     [:div {:key key, :id (str "item-" key)}
      [sut/text item-f :id :disabled true]])])

(def data
  {1 {:id 1}
   2 {:id 2}
   3 {:id 3}})

(t/deftest render
  (let [f  (ctx/build data utils.blank/errors utils.blank/update)
        el (form-builder f)
        _  (utils.render/render el)]
    (t/testing "key"
      (doseq [[key item] data
              :let       [item-div (utils.render/query-selector (str "#item-" key))]]
        (t/is (some? item-div))))
    (t/testing "nested input values"
      (doseq [[key item] data
              :let       [input (utils.render/path-selector [key :id] "input")]]
        (t/is (= (str (:id item))
                 (.-value input)))))))

(comment
  (t/run-tests))
