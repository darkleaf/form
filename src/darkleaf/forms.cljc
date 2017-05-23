(ns darkleaf.forms
  (:require [hiccup.core :as hiccup]))


(defn nested-ctx [ctx key]
  (-> ctx
      (update :data get key)
      (update :path conj key)
      (update :name-prefix str "[" (name key) "]")))

;; multimethod?
(defn child-ctxs [{:keys [data errors path name-prefix]}]
  (cond
    (map? data)
    (map
     (fn [[key item]]
       {:data item
        :errors errors
        :path (conj path key)
        :name-prefix (str name-prefix "[" (name key) "]")})
     data)

    :else
    (map-indexed
     (fn [idx item]
       {:data item
        :errors errors
        :path (conj path idx)
        :name-prefix (str name-prefix "[]")})
     data)))

(defn render-child [ctx body]
  (reduce
   (fn [div item]
     (conj div (item ctx)))
   [:div]
   body))

(defn nested [key render & body]
  (fn [ctx]
    (as-> ctx <>
      (nested-ctx <> key)
      (child-ctxs <>)
      (map #(render-child % body) <>))))


(defn form [& body]
  (fn [data errors]
    (reduce
     (fn [form item]
       (conj form (item {:data data
                         :errors errors
                         :path []
                         :name-prefix "prefix"})))
     [:form]
     body)))


(defn text-input [key]
  (fn [{:keys [data errors path name-prefix]}]
    (let [path (conj path key)]
      [:div
       [:input {:type "text"
                :name (str name-prefix "[" (name key) "]")
                :value (get data key)}]
       (into [:div] (get errors path []))])))



(let [data {:title "Awesome"
            :comments [{:text "foo"
                        :author "bar"}]}
      errors {[:title] ["can't be blank"]}

      form-ex (form
               (text-input :title)
               (nested :comments
                       (text-input :text)
                       (text-input :author)))
      hicc (form-ex data errors)]
  (hiccup/html hicc))
