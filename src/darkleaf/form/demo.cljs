(ns darkleaf.form.demo
  (:require
   [clojure.string :as string]
   [cljs.spec.alpha :as s]
   [reagent.core :as r]
   [cljsjs.react-debounce-input]
   [goog.object :as gobj]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.spec-integration :refer [explain-data->errors]]
   [clojure.string :as str]))

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

(s/def ::inst inst?)
(s/def ::present-string (s/and string? #(not (string/blank? %))))
(s/def ::pos-int pos-int?)

(s/def ::name ::present-string)
(s/def ::created-at ::inst)
(s/def ::id ::pos-int)
(s/def ::complexity ::pos-int)
(s/def ::task (s/keys :req-un [::id ::name ::complexity]))
(s/def ::tasks (s/coll-of ::task))
(s/def ::project (s/keys :req-un [::name ::created-at ::tasks]))


(def debounce-input (r/adapt-react-class js/DebounceInput))

(defn- event->value [e]
  (gobj/getValueByKeys e "target" "value"))

(defn- class-names [& names]
  (->> names
       (remove nil?)
       (string/join " ")))

(defn input [-ctx id label]
  (let [ctx (ctx/nested -ctx id)
        value (ctx/get-data ctx)
        errors (ctx/get-errors ctx)
        has-errors? (not-empty errors)]
    [:div {:class (class-names
                   "form-group"
                   (if has-errors? "has-danger"))}
     [:label.form-control-label label]
     [debounce-input {:class-name "form-control"
                      :type :text
                      :value value
                      :on-change #(ctx/set-data ctx (event->value %))}]
     (for [error errors]
       ^{:key error} [:div.form-control-feedback error])]))

(defn component []
  (let [data (r/atom initial-data)]
    (fn []
      (let [errors (->> @data (s/explain-data ::project) (explain-data->errors))
            f (ctx/build @data errors #(reset! data %))]
        [:form
         [input f :name "Name"]
         [input f :created-at "Created at"]
         [:h2 "Tasks"]
         [:div.row
          (for [[idx task-f] (ctx/nested f :tasks)]
            [:div.col-sm-12.my-3 {:key idx}
             [:div.card
              [:div.card-block
               [input task-f :name "Name"]
               [input task-f :complexity "Complexity"]]]])]
         [:div
          [:p (str @data)]
          [:p (str errors)]]]))))

(r/render [component]
          (.getElementById js/document "point"))
