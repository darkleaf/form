(ns darkleaf.form.spec-integration
  (:require
   [cljs.spec.alpha :as s]
   [darkleaf.form.context :as ctx]))

(defn explain-data->errors [exp]
  (let [problems (get exp ::s/problems '())]
    (reduce
     (fn [acc x]
       (let [in (:in x)
             path (conj in ctx/errors-key)
             error (-> x :via last)]
         (update-in acc path conj error)))
     {}
     problems)))
