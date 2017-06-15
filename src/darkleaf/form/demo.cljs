(ns darkleaf.form.demo
  (:require
   [reagent.core :as r]
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

(defn input [-ctx id label]
  (let [ctx (ctx/nested -ctx id)
        value (ctx/get-data ctx)]
    [:div.form-group
     [:label label]
     [:input.form-control {:type :text
                           :value value
                           :on-change #(ctx/set-data ctx
                                                     (gobj/getValueByKeys %
                                                                          "target"
                                                                          "value"))}]
     [:small.form-text.text-muted "Required"]]))

(defn component []
  (let [data (r/atom initial-data)]
    (fn []
      (let [f (ctx/build @data nil #(reset! data %))]
        [:form
         [input f :name "Name"]
         [input f :created-at "Created at"]
         [:h2 "Tasks"]
         [:div.row
          (for [[k task-f] (ctx/nested f :t-asks)]
            [:div.col-sm-12.my-3 {:key k}
             [:div.card
              [:div.card-block
               [input task-f :name "Name"]]]])]]))))

(r/render [component]
          (.getElementById js/document "point"))
