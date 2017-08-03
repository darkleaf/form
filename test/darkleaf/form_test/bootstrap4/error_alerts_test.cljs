(ns darkleaf.form-test.bootstrap4.error-alerts-test
  (:require
   [darkleaf.form.bootstrap4 :as sut]
   [cljs.test :as t :include-macros true]
   [clojure.string :as string]
   [darkleaf.form.context :as ctx]
   [darkleaf.form-test.utils.render :as utils.render]
   [darkleaf.form-test.utils.blank :as utils.blank]))

(t/use-fixtures :each utils.render/container-fixture)

(defn form-builder [f]
  (let [items-f (ctx/nested f :items)]
    [:div
     [sut/error-alerts items-f]]))

(def data
  {:items []})

(def message-id :must-contains-at-least-one-item)

(def errors
  {:items {ctx/errors-key [message-id]}})

(t/deftest render
  (let [f    (ctx/build data errors utils.blank/update)
        el   (form-builder f)
        _    (utils.render/render el)
        html (utils.render/get-html)]
    (t/is (string/includes? html message-id))))

(t/deftest render-with-i18n
  (let [message "Must contains at least one item"
        i18n (fn [path error]
               (when (= error message-id)
                 message))
        f    (ctx/build data errors utils.blank/update {:error i18n})
        el   (form-builder f)
        _    (utils.render/render el)
        html (utils.render/get-html)]
    (t/is (string/includes? html message))))

(comment
  (t/run-tests))
