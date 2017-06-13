(ns darkleaf.form.demo
  (:require
   #_[reagent.core :as r]
   [rum.core :as rum]
   [cljsjs.react-debounce-input]
   [goog.object :as gobj]
   [goog.string :as gstr]
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

(defn opts->js [opts]
  (reduce-kv
   (fn [acc k v]
     (gobj/set acc
               (gstr/toCamelCase (name k))
               v)
     acc)
   (js-obj)
   opts))

(defn debounce-input [opts]
  (js/React.createElement js/DebounceInput (opts->js opts)))

(rum/defc input [id label]
  (ctx/receiver
   (fn [-ctx]
     (let [ctx (ctx/conj-path -ctx id)
           value (ctx/get-data ctx)]
       [:div.form-group
        [:label label]

        (debounce-input {:class-name "form-control"
                         :debounce-timeout 300
                         :value value
                         :on-change #(ctx/set-data
                                      ctx
                                      (gobj/getValueByKeys %
                                                           "target"
                                                           "value"))})

        [:small.form-text.text-muted "Required"]]))))

(rum/defc nested [id tag opts item]
  (ctx/receiver
   (fn [-ctx]
     (let [ctx (ctx/conj-path -ctx id)]
       (ctx/reduce-data
        ctx
        (fn [acc i-ctx]
          (conj acc
                (rum/with-key
                  (ctx/provider i-ctx item)
                  (ctx/get-id i-ctx))))
        [tag opts])))))

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
                 (input :name "Name")]]])
      (str @data)])))

(rum/mount (component) (.getElementById js/document "point"))
