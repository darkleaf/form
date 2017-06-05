(ns darkleaf.form.bootstrap4
  (:require
   #_[hiccup.core :as hiccup]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.abstract :as abstract]))

(defn form [& renderers]
  (fn [data errors form-opts]
    (let [ctx (ctx/build data errors "form")
          elements (map #(% ctx) renderers)
          form-element [:form form-opts]]
      (into form-element elements))))

(defn text-input [id]
  (fn [initial-ctx]
    (let [ctx (ctx/conj-path initial-ctx id)]
      [:div
       (abstract/input ctx :text)
       #_(into [:div] (ctx/get-errors ctx))])))

(defn render-child [ctx renderers]
  (into [:div] (map #(% ctx) renderers)))

(defn nested [id & renderers]
  (fn [initial-ctx]
    (let [ctx (ctx/conj-path initial-ctx id)
          data (ctx/get-data ctx)
          elements (ctx/reduce
                    ctx
                    (fn [acc ctx]
                      (conj acc
                            (render-child ctx renderers)))
                    [])]
      (into [:div] elements))))

#_(let [data {:title "Awesome"
              :comments [{:text "foo"
                          :author "bar"}]}
        errors {[:title] ["can't be blank"]}
        renderer (form
                  (text-input :title)
                  (nested :comments
                          (text-input :text)
                          (text-input :author)))
        markup (renderer data errors)]
    (hiccup/html markup))
