(ns darkleaf.form.demo
  (:require
   #_[reagent.core :as r]
   [rum.core :as rum]
   [goog.object :as gobj]
   [darkleaf.form.context :as ctx]))

(enable-console-print!)

(def initial-data
  {:name "Some cool projuect"
   :created-at #inst "2017-01-01"
   :tasks [{:id 1
            :name "todo 1"
            :complexity 2}
           {:id 2
            :name "todo 2"
            :complexity 3}]})

(rum/defc input-impl [-ctx id label]
  (let [ctx (ctx/conj-path -ctx id)
        value (ctx/get-data ctx)]
    [:div.form-group
     [:label label]
     [:input.form-control {:type :text
                           :value value
                           :on-change #(ctx/set-data
                                        ctx
                                        (gobj/getValueByKeys %
                                                             "target"
                                                             "value"))}]
     [:small.form-text.text-muted "Required"]]))

(rum/defc input [& args]
  (apply ctx/receiver input-impl args))


(rum/defc nested-impl [-ctx id tag opts item]
  (let [ctx (ctx/conj-path -ctx id)]
    (ctx/reduce-data
     ctx
     (fn [acc i-ctx]
       (conj acc (ctx/provider i-ctx item)))
     [tag opts])))

(rum/defc nested [& args]
  (apply ctx/receiver nested-impl args))

(rum/defcs component < (rum/local initial-data ::data)
  [state]
  (let [data (::data state)]
    (ctx/provider
     (ctx/build @data {} #(reset! data %))
     [:form
      (input :name "Name")
      (input :created-at "Created at")
      [:h2 "Tasks"]
      (nested :tasks :div.row {}
              [:div.col-sm-12.my-3
               [:div.card
                [:div.card-block
                 (input :name "Name")]]])])))

(rum/mount (component) (.getElementById js/document "point"))
