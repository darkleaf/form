(ns darkleaf.form.js
  (:require
   [darkleaf.form.js-fix]
   [darkleaf.form.nested-params :as nested-params]
   [darkleaf.form.keyword-params :as keyword-params]
   [darkleaf.form.bootstrap4 :as b]
   [reagent.core :as r]))

(defn component []
  (let [data {:title "Awesome"
              :comments [{:text "foo"
                          :author "bar"}]}
        errors {[:title] ["can't be blank"]}
        renderer (b/form
                  (b/text-input :title)
                  (b/nested :comments
                            (b/text-input :text)
                            (b/text-input :author)))
        markup (renderer data errors)]
    markup))

(defn form-params [node]
  (let [form-data (js/FormData. node)
        entries (.entries form-data)]
    (->> entries
         (es6-iterator-seq)
         (map js->clj)
         (into {}))))

(defn form-data [node]
  (-> (form-params node)
      (nested-params/nest-params)
      (keyword-params/keyify-params)))

(let [utils js/React.addons.TestUtils
      element (r/as-element [component])
      x (.renderIntoDocument utils element)
      node (.findRenderedDOMComponentWithTag utils x "form")]

  (form-data node))
