(ns darkleaf.form-test.bootstrap4.nested-vector-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [darkleaf.form.context :as ctx]
   [darkleaf.form-test.utils.render :as utils.render]
   [darkleaf.form-test.utils.blank :as utils.blank]))

(t/use-fixtures :each utils.render/container-fixture)

(defn form-builder [f]
  (let [items-f (ctx/nested f :items)]
    [:div
     (for [[idx item-f] items-f
           :let [id (-> item-f (ctx/get-data) :id)]]
       [:div {:key id, :id (str "item-" id), :data-idx idx}
        [sut/text item-f :id :disabled true]])]))

(def data
  {:items [{:id 1}
           {:id 2}
           {:id 3}]})

(t/deftest render
  (let [f     (ctx/build data utils.blank/errors utils.blank/update)
        el    (form-builder f)
        _     (utils.render/render el)
        items (:items data)]
    (t/testing "idx"
      (doseq [[idx item] (map-indexed vector items)
              :let       [item-id (str "#item-" (:id item))
                          item-div (utils.render/query-selector item-id)
                          item-idx (.. item-div -dataset -idx)]]
        (t/is (= (str idx)
                 item-idx))))
    (t/testing "nested input values"
      (doseq [[idx item] (map-indexed vector items)
              :let       [input (utils.render/path-selector [:items idx :id] "input")]]
        (t/is (= (str (:id item))
                 (.-value input)))))))

(comment
  (t/run-tests))
