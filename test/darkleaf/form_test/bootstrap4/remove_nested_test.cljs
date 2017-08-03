(ns darkleaf.form-test.bootstrap4.remove-nested-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [darkleaf.form.context :as ctx]
   [darkleaf.form-test.utils.render :as utils.render]
   [darkleaf.form-test.utils.blank :as utils.blank]
   [darkleaf.form-test.utils.common-checks :as utils.common-checks]))

(t/use-fixtures :each utils.render/container-fixture)

(defn form-builder [f]
  (let [items-f (ctx/nested f :items)]
    [:div
     (for [[idx _item-f] items-f]
       [:div {:key idx}
        [sut/remove-nested-btn items-f idx :id (str "remove-" idx)]])]))

(def data
  {:items [{:id 1}
           {:id 2}
           {:id 3}]})

(t/deftest render
  (let [f  (ctx/build data utils.blank/errors utils.blank/update)
        el (form-builder f)
        _  (utils.render/render el)]
    (doseq [[idx item] (map-indexed vector data)
            :let       [item-div (utils.render/query-selector (str "#remove-" idx))]]
      (t/is (some? item-div)))))

(t/deftest click
  (utils.common-checks/change-data-by-click
   form-builder "#remove-0"
   data
   (update data :items rest)))

(comment
  (t/run-tests))
