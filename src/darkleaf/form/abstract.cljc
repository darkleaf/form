(ns darkleaf.form.abstract
  (:require
   [darkleaf.form.context :as ctx]))

;; (defn nested-ctx [ctx key]
;;   (-> ctx
;;       (update :data get key)
;;       (update :path conj key)
;;       (update :name-prefix str "[" (name key) "]")))

;; ;; multimethod?
;; (defn child-ctxs [{:keys [data errors path name-prefix]}]
;;   (map-indexed
;;      (fn [idx item]
;;        {:data item
;;         :errors errors
;;         :path (conj path idx)
;;         :name-prefix (str name-prefix "[]")})
;;      data))


;; (defn render-child [ctx body]
;;   (reduce
;;    (fn [div item]
;;      (conj div (item ctx)))
;;    [:div]
;;    body))

;; (defn nested-sequential [key render body]
;;   (fn [ctx]
;;     (as-> ctx <>
;;       (nested-ctx <> key)
;;       (child-ctxs <>)
;;       (map #(render-child % body) <>))))


;; form-item data errors -> element





(defn input [ctx type]
  [:input {:type type
           :name (ctx/get-name ctx)
           :value (ctx/get-data ctx)}])
