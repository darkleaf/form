(ns darkleaf.form.demo
  (:require
   [cljs.spec.alpha :as s]
   [cljs.pprint :refer [pprint]]
   [clojure.string :as string]
   [reagent.core :as r]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.spec-integration :refer [explain-data->errors]]
   [darkleaf.form.bootstrap4 :as bootstrap]))

(enable-console-print!)


(s/def ::string string?)
(s/def ::present-string (s/and string? #(not (string/blank? %))))
(s/def ::email #(re-matches #"^\S+@\S+$" %))
(s/def ::password #(< 8 (count %)))

(s/def :root/example-text ::present-string)
(s/def :root/example-password (s/and ::present-string ::password))
(s/def :root/example-textarea ::string)
(s/def :root/example-checkbox true?)

(s/def ::data (s/keys :req [:root/example-text
                            :root/example-password
                            :root/example-textarea
                            :root/example-checkbox]))

(def initial-data
  {:root/example-text "Some text"
   :root/example-password "Some password"
   :root/example-textarea "Some\nbig\ntext"
   :root/example-select "second"
   :root/example-multipleselect []
   :root/example-checkbox true
   :root/nested [{:nested/id 1, :nested/example-text "first"}
                 {:nested/id 2, :nested/example-text "second"}]})

(defn component []
  (let [data (r/atom initial-data)]
    (fn []
      (let [errors (->> @data (s/explain-data ::data) (explain-data->errors))
            f (ctx/build @data errors #(reset! data %))]
        [:div.row
         [:div.col-sm-6
          [:form
           [bootstrap/text f :root/example-text]
           [bootstrap/text f :root/example-password :type :password]
           [bootstrap/textarea f :root/example-textarea :rows 5]
           [bootstrap/select f :root/example-select :options [["first" "First"]
                                                              ["second" "Second"]]]
           [bootstrap/multi-select f :root/example-multipleselect
            :options [["first" "First"]
                      ["second" "Second"]]]

           [bootstrap/checkbox f :root/example-checkbox]

           [:h2 "Tasks"]
           [:div.row
            (for [[_idx task-f] (ctx/nested f :root/nested)]
              [:div.col-sm-12.my-3 {:key (-> task-f (ctx/get-data) :nested/id)}
               [:div.card
                [:div.card-block
                 [bootstrap/text task-f :nested/example-text]]]])]]]
         [:div.col-sm-6
          [:div.card.mb-3
           [:div.card-header "Data"]
           [:div.card-block
            [:pre [:code (with-out-str (pprint @data))]]]]
          [:div.card.mb-3
           [:div.card-header "errors"]
           [:div.card-block
            [:pre [:code (with-out-str (pprint errors))]]]]]]))))

(r/render [component]
          (.getElementById js/document "point"))
