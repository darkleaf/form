(ns darkleaf.form.demo
  (:require
   [reagent.core :as r]
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

(def input
  (ctx/receiver
   (fn [-ctx id label]
     (let [ctx (ctx/conj-path -ctx id)
           value (ctx/get-data ctx)]
       [:div.form-group
        [:label label]
        [:input.form-control {:type :text, :value value}]
        [:small.form-text.text-muted "Required"]]))))

(def nested
  (ctx/receiver
   (fn [-ctx id tag opts item]
     (let [ctx (ctx/conj-path -ctx id)]
       (ctx/reduce-data
        ctx
        (fn [acc i-ctx]
          (conj acc [ctx/provider i-ctx item]))
        [tag opts])))))

(defn component []
  [ctx/provider (ctx/build initial-data {} prn)
   [:form
    [input :name "Name"]
    [input :created-at "Created at"]

    [:h2 "Tasks"]
    [nested :tasks :div.row {}
     [:div.col-sm-12.my-3
      [:div.card
       [:div.card-block
        [input :name "Name"]]]]]]])

(r/render [component]
          (.getElementById js/document "point"))
