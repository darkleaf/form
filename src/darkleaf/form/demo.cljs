(ns darkleaf.form.demo
  (:require
   [cljs.spec.alpha :as s]
   [cljs.spec.gen.alpha :as gen]
   [clojure.test.check.generators]

   [cljs.pprint :refer [pprint]]
   [clojure.string :as string]
   [reagent.core :as r]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.spec-integration :refer [explain-data->errors]]
   [darkleaf.form.bootstrap4 :as bootstrap]))

(enable-console-print!)


(s/def ::string string?)
(s/def ::present-string (s/and string? #(not (string/blank? %))))
#_(s/def ::email #(re-matches #"^\S+@\S+$" %))
(s/def ::password #(< 8 (count %)))

(defn add-nested-item [data]
  (update data :root/nested
          conj {:nested/id (random-uuid)
                :nested/example-text ""}))

(defn- build-ctx [data-atom spec]
  (let [data @data-atom
        errors (->> data (s/explain-data spec) (explain-data->errors))
        update (fn [path f] (swap! data-atom update-in path f))]
    (ctx/build data errors update)))


(s/def :text/example-text ::present-string)
(s/def :text/example-password (s/and ::present-string ::password))
(s/def :text/data (s/keys :req [:text/example-text
                                :text/example-password]))
(defn text [data-atom]
  (let [f (build-ctx data-atom :text/data)]
    [:form
     [bootstrap/text f :text/example-text]
     [bootstrap/text f :text/example-password :type :password]]))

(s/def :textarea/example ::present-string)
(s/def :textarea/data (s/keys :req [:textarea/example]))
(defn textarea [data-atom]
  (let [f (build-ctx data-atom :textarea/data)]
    [:form
     [bootstrap/textarea f :textarea/example :rows 5]]))

(s/def :select/example #{"foo" "bar"})
(s/def :select/data (s/keys :req [:select/example]))
(defn select [data-atom]
  (let [f (build-ctx data-atom :select/data)]
    [bootstrap/select f :select/example
     :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]]))

(s/def :radio-select/example #{"foo" "bar"})
(s/def :radio-select/data (s/keys :req [:radio-select/example]))
(defn radio-select [data-atom]
  (let [f (build-ctx data-atom :radio-select/data)]
    [bootstrap/radio-select f :radio-select/example
     :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]]))

(s/def :multi-select/example (s/with-gen
                               (s/and vector? #(every? #{"foo" "bar"} %) distinct?)
                               #(gen/vector-distinct (gen/elements #{"foo" "bar"}))))
(s/def :multi-select/data (s/keys :req [:multi-select/example]))
(defn multi-select [data-atom]
  (let [f (build-ctx data-atom :multi-select/data)]
    [bootstrap/multi-select f :multi-select/example
     :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]]))

(s/def :checkbox/example true?)
(s/def :checkbox/data (s/keys :req [:checkbox/example]))
(defn checkbox [data-atom]
  (let [f (build-ctx data-atom :checkbox/data)]
    [bootstrap/checkbox f :checkbox/example]))

(s/def :nested/id uuid?)
(s/def :nested/attribute ::present-string)
(s/def :nested/item (s/keys :req [:nested/id :nested/attribute]))
(s/def :nested/items (s/coll-of :nested/item))
(s/def :nested/data (s/keys :req [:nested/items]))
(defn nested [data-atom]
  (let [f (build-ctx data-atom :nested/data)
        items-f (ctx/nested f :nested/items)]
    [:div
     (for [[idx item-f] items-f
           :let [id (-> item-f (ctx/get-data) :nested/id)]]
       [:div.my-3 {:key id}
        [:div.card
         [:div.card-block
          [bootstrap/text item-f :nested/id :disabled true]
          [bootstrap/text item-f :nested/attribute]
          [bootstrap/remove-nested items-f idx
           :class "btn-outline-danger btn-sm float-right"
           :text "delete"]]]])]))




#_(defn component []
    (let [data (r/atom initial-data)]
      (fn []
        (let [errors (->> @data (s/explain-data ::data) (explain-data->errors))
              update (fn [path f] (swap! data update-in path f))
              f (ctx/build @data errors update)]
          [:div.row
           [:div.col-sm-6
            [:form
             [:div
              [:h2 "Nested items"]
              [:div.row
               (for [[idx task-f] (ctx/nested f :root/nested)
                     :let [id (-> task-f (ctx/get-data) :nested/id)]]
                 [:div.col-sm-12.my-3 {:key id}
                  [:div.card
                   [:div.card-block
                    [bootstrap/text task-f :nested/example-text]
                    [:button.btn.btn-outline-danger.btn-sm.float-right
                     {:type :button
                      :on-click #(swap! data delete-nested-item idx)}
                     "delete"]]]])]
              [:button.btn.btn-primary.btn-sm
               {:type :button
                :on-click #(swap! data add-nested-item)}
               "add nested"]]]]]))))

(defn- inspect [data-atom spec]
  (let [data @data-atom
        errors (->> data (s/explain-data spec) (explain-data->errors))]
    [:div
     [:div.card.mb-3
      [:div.card-header "Data"]
      [:div.card-block
       [:pre [:code (with-out-str (pprint data))]]]]
     [:div.card.mb-3
      [:div.card-header "Errors"]
      [:div.card-block
       [:pre [:code (with-out-str (pprint errors))]]]]]))

(defn- container [title spec component]
  (let [data (-> spec s/gen gen/generate)
        data-atom (r/atom data)]
    (fn []
      [:div
       [:h2 title]
       [component data-atom]
       [inspect data-atom spec]])))

(defn component []
  [:div
    [container "Text" :text/data text]
    [container "Textarea" :textarea/data textarea]
    [container "Select" :select/data select]
    [container "Radio select" :radio-select/data radio-select]
    [container "Multi select" :multi-select/data multi-select]
    [container "Checkbox" :checkbox/data checkbox]
    [container "Nested" :nested/data nested]])


(r/render [component]
          (.getElementById js/document "point"))
