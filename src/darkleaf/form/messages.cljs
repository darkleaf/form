(ns darkleaf.form.messages
  (:require
   [clojure.string :as string]
   [darkleaf.form.context :as ctx]))

(defn label [ctx]
  (let [path (ctx/get-path ctx)
        id (last path)]
    (-> id
        (name)
        (string/replace "-" " ")
        (string/capitalize))))

(defn i18n-errors-fallback [path error]
  (str error))

(defn error [ctx error]
  (let [i18n (ctx/get-i18n ctx)
        i18n-errors (get i18n :errors (constantly nil))
        path (ctx/get-path ctx)]
    (or
     (i18n-errors path error)
     (i18n-errors-fallback path error))))
