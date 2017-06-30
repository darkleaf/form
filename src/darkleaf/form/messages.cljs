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

(defn error [ctx error]
  (str error))
