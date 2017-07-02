(ns darkleaf.form.messages
  (:require
   [clojure.string :as string]
   [darkleaf.form.context :as ctx]))

(defn- i18n-label-fallback [path]
  (-> (last path)
      (name)
      (string/replace "-" " ")
      (string/capitalize)))

(defn label [ctx]
  (let [i18n (ctx/get-i18n ctx)
        i18n-label (get i18n :label (constantly nil))
        path (ctx/get-path ctx)]
    (or
     (i18n-label path)
     (i18n-label-fallback path))))

(defn- i18n-error-fallback [path error]
  (str error))

(defn error [ctx error]
  (let [i18n (ctx/get-i18n ctx)
        i18n-error (get i18n :error (constantly nil))
        path (ctx/get-path ctx)]
    (or
     (i18n-error path error)
     (i18n-error-fallback path error))))
