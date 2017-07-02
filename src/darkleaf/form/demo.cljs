(ns darkleaf.form.demo
  (:require
   [cljs.spec.alpha :as s]
   [cljs.spec.gen.alpha :as gen]
   [clojure.test.check.generators]
   [cljs.core.match :refer-macros [match]]
   [cljs.pprint :refer [pprint]]
   [clojure.string :as string]
   [reagent.core :as r]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.spec-integration :refer [explain-data->errors]]
   [darkleaf.form.bootstrap4 :as bootstrap]))

(enable-console-print!)

(s/def ::string string?)
(s/def ::present-string (s/and ::string #(not (string/blank? %))))
(s/def ::password (s/and ::string #(or (empty? %) (< 8 (count %)))))

(s/def :text/required-text ::present-string)
(s/def :text/required-password (s/and ::present-string ::password))
(s/def :text/optional-password ::password)
(s/def :text/data (s/keys :req [:text/required-text
                                :text/required-password
                                :text/optional-password]))
(defn text [f]
  [:form
   [bootstrap/text f :text/required-text]
   [bootstrap/text f :text/required-password :type :password]
   [bootstrap/text f :text/optional-password :type :password]])

(s/def :textarea/example ::present-string)
(s/def :textarea/data (s/keys :req [:textarea/example]))
(defn textarea [f]
  [:form
   [bootstrap/textarea f :textarea/example :rows 5]])

(s/def :select/example #{"foo" "bar"})
(s/def :select/data (s/keys :req [:select/example]))
(defn select [f]
    [bootstrap/select f :select/example
     :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]])

(s/def :radio-select/example #{"foo" "bar"})
(s/def :radio-select/data (s/keys :req [:radio-select/example]))
(defn radio-select [f]
  [bootstrap/radio-select f :radio-select/example
   :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]])

(s/def :multi-select/example (s/with-gen
                               (s/and vector? #(every? #{"foo" "bar"} %) distinct?)
                               #(gen/vector-distinct (gen/elements #{"foo" "bar"}))))
(s/def :multi-select/data (s/keys :req [:multi-select/example]))
(defn multi-select [f]
  [bootstrap/multi-select f :multi-select/example
   :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]])

(s/def :checkbox/example true?)
(s/def :checkbox/data (s/keys :req [:checkbox/example]))
(defn checkbox [f]
  [bootstrap/checkbox f :checkbox/example])

(s/def :nested/id uuid?)
(s/def :nested/attribute ::present-string)
(s/def :nested/item (s/keys :req [:nested/id :nested/attribute]))
(s/def :nested/items (s/coll-of :nested/item :min-count 3, :max-count 4))
(s/def :nested/data (s/keys :req [:nested/items]))
(defn nested [f]
  (let [items-f (ctx/nested f :nested/items)
        build-blank-item (fn [] {:nested/id (random-uuid)
                                 :nested/attribute ""})]

    [:div.my-3
     (for [[idx item-f] items-f
           :let [id (-> item-f (ctx/get-data) :nested/id)]]
       [:div.my-3 {:key id}
        [:div.card
         [:div.card-block
          [bootstrap/text item-f :nested/id :disabled true]
          [bootstrap/text item-f :nested/attribute]
          [bootstrap/remove-nested items-f idx
           :class "btn-outline-danger btn-sm float-right"
           :text "delete"]]]])
     [bootstrap/error-alerts items-f]
     [bootstrap/add-nested items-f build-blank-item
      :class "btn-primary btn-sm"]]))

(defn- inspect [f]
  (let [data (ctx/get-data f)
        errors (ctx/get-errors-subtree f)]
    [:div
     [:div.card.mb-3
      [:div.card-header "Data"]
      [:div.card-block
       [:pre [:code (with-out-str (pprint data))]]]]
     [:div.card.mb-3
      [:div.card-header "Errors"]
      [:div.card-block
       [:pre [:code (with-out-str (pprint errors))]]]]]))

(defn- i18n-error [path error]
  (condp = error
    ::present-string "can't be blank"
    ::password "is too short"
    :select/example "can't be Buzz"
    :radio-select/example "can't be Buzz"
    :multi-select/example "can't contain Buzz"
    :checkbox/example "must be checked"
    :nested/items "must contain 3 or 4 items"
    nil))

(defn- i18n-label [path]
  (match path
         [:nested/items 0 :nested/id] "First item UUID"
         [:nested/items _ :nested/id] "UUID"
         :else nil))

(defn- build-ctx [data-atom spec]
  (let [data @data-atom
        errors (->> data (s/explain-data spec) (explain-data->errors))
        update (fn [path f] (swap! data-atom update-in path f))]
    (ctx/build data errors update
               {:error i18n-error
                :label i18n-label})))

(defn- container [title spec component]
  (let [data (-> spec s/gen gen/generate)
        data-atom (r/atom data)]
    (fn []
      (let [f (build-ctx data-atom spec)]
        [:div
         [:h2 title]
         [component f]
         [inspect f]]))))

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
