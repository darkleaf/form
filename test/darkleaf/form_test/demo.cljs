(ns darkleaf.form-test.demo
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
   [darkleaf.form.bootstrap4 :as bootstrap]
   [darkleaf.form-test.meta-test :as meta-test]))

;; TODO: move to demo folder

(enable-console-print!)

(s/def ::string string?)
(s/def ::present-string (s/and ::string #(not (string/blank? %))))
(s/def ::password (s/and ::string #(or (empty? %) (< 8 (count %)))))

(s/def :text/attribute ::present-string)
(s/def :text/data (s/keys :req [:text/attribute]))
(defn text [f]
  [:form
   [bootstrap/text f :text/attribute]])

(s/def :password/attribute (s/and ::present-string ::password))
(s/def :password/data (s/keys :req [:password/attribute]))
(defn password [f]
  [:form
   [bootstrap/text f :password/attribute :type :password]])

(s/def :textarea/attribute ::present-string)
(s/def :textarea/data (s/keys :req [:textarea/attribute]))
(defn textarea [f]
  [:form
   [bootstrap/textarea f :textarea/attribute :rows 5]])

(s/def :select/attribute #{"foo" "bar"})
(s/def :select/data (s/keys :req [:select/attribute]))
(defn select [f]
    [bootstrap/select f :select/attribute
     :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]])

(s/def :radio-select/attribute #{"foo" "bar"})
(s/def :radio-select/data (s/keys :req [:radio-select/attribute]))
(defn radio-select [f]
  [bootstrap/radio-select f :radio-select/attribute
   :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]])

(s/def :multi-select/attribute (s/with-gen
                                 (s/and vector? #(every? #{"foo" "bar"} %) distinct?)
                                 #(gen/vector-distinct (gen/elements #{"foo" "bar"}))))
(s/def :multi-select/data (s/keys :req [:multi-select/attribute]))
(defn multi-select [f]
  [bootstrap/multi-select f :multi-select/attribute
   :options [["foo" "Foo"] ["bar" "Bar"] ["buzz" "Buzz"]]])

(s/def :checkbox/attribute true?)
(s/def :checkbox/data (s/keys :req [:checkbox/attribute]))
(defn checkbox [f]
  [bootstrap/checkbox f :checkbox/attribute])

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
    :select/attribute "can't be Buzz"
    :radio-select/attribute "can't be Buzz"
    :multi-select/attribute "can't contain Buzz"
    :checkbox/attribute "must be checked"
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

(defn- container [title component spec]
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
   [container "Text" text :text/data]
   [container "Password" password :password/data]
   [container "Textarea" textarea :textarea/data]
   [container "Select" select :select/data]
   [container "Radio select" radio-select :radio-select/data]
   [container "Multi select" multi-select :multi-select/data]
   [container "Checkbox" checkbox :checkbox/data]
   [container "Nested" nested :nested/data]])

(r/render [component]
          (.getElementById js/document "point"))

(meta-test/run-all)
