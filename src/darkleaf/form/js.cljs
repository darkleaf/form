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

(defn form-data-entries [form-data]
  (reduce
   (fn [acc x]
     (let [key (aget x 0)
           val (aget x 1)]
       (assoc acc key val)))
   {}
   (-> form-data
       (.entries)
       (es6-iterator-seq))))

(defn fetch-form-data [form]
  (let [form-data (js/FormData. form)
        entries (form-data-entries form-data)]
    (nested-params/nest-params entries)))


(let [utils js/React.addons.TestUtils
      element (r/as-element [component])
      x (.renderIntoDocument utils element)
      node (.findRenderedDOMComponentWithTag utils x "form")]
  (-> (fetch-form-data node)
      (keyword-params/keyify-params)))
