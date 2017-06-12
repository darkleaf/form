(ns darkleaf.form.js
  (:require
   [darkleaf.form.nested-params :as nested-params]
   [darkleaf.form.keyword-params :as keyword-params]
   [darkleaf.form.bootstrap4 :as b]
   [reagent.core :as r]
   [goog.object :as gobj]))

(enable-console-print!)


(defn form-params [node]
  (let [form-data (js/FormData. node)
        entries (.entries form-data)]
    (->> entries
         (es6-iterator-seq)
         (map js->clj)
         ((fn [x]
            (prn x)
            x))
         #_(into (array-map))
         (reduce
          (fn [acc [k v]]
            (prn (type acc))

            (assoc acc k v))
          {})
         ((fn [x]
            (prn x)
            x)))))

(defn form-data [node]
  (-> (form-params node)
      (nested-params/nest-params)
      (keyword-params/keyify-params)))

(defn component []
  (let [data (r/atom {:title "User 1"
                      :posts {"1" {:title "hello"
                                   :comments {"1" {:text "foo"
                                                   :author "bar"}
                                              "2" {:text "foo 2"
                                                   :author "bar 2"}
                                              "3" {:text "foo 3"
                                                   :author "bar 3"}}}
                              "2" {:title "hello 2"
                                   :comments {"1" {:text "foo"
                                                   :author "bar"}
                                              "2" {:text "foo 2"
                                                   :author "bar 2"}
                                              "3" {:text "foo 3"
                                                   :author "bar 3"}}}}})

        errors {[:title] ["can't be blank"]}
        renderer (b/form
                  (b/text-input :title)
                  (b/nested :posts
                            (b/text-input :title)
                            (b/nested :comments
                                      (b/text-input :text)
                                      (b/text-input :author))))
        form-opts {:on-change
                   (fn [e]
                     (let [form (gobj/getValueByKeys e "target" "form")
                           new-data (:form (form-data form))]

                       (reset! data new-data)
                       #_(prn new-data)))}]
    (fn []
      (renderer @data errors form-opts))))

(r/render [component]
          (.getElementById js/document "point"))


#_(let [utils js/React.addons.TestUtils
        element (r/as-element [component])
        x (.renderIntoDocument utils element)
        node (.findRenderedDOMComponentWithTag utils x "form")]

    (form-data node))
