(ns darkleaf.form.context
  (:require
   [goog.object :as gobj]
   [reagent.core :as r]))

(def ^:private ctx-key "ctx")

(def provider
  (r/create-class
   {:displayName
    "ContextProvider"

    :getChildContext
    (fn []
      (this-as this
        (let [[_ value _child] (gobj/getValueByKeys this "props" "argv")]
          (js-obj ctx-key value))))

    :childContextTypes
    (js-obj ctx-key (gobj/getValueByKeys js/React "PropTypes" "any" "isRequired"))

    :reagent-render
    (fn [_value child]
      child)}))

(defn receiver [component]
  "Higher-Order Component"
  (r/create-class
   {:displayName
    "ContextReceiver"

    :shouldComponentUpdate
    (fn [] true)

    :contextTypes
    (js-obj ctx-key (gobj/getValueByKeys js/React "PropTypes" "any" "isRequired"))

    :reagent-render
    (fn [& args]
      (let [this (r/current-component)
            value (gobj/getValueByKeys this "context" ctx-key)]
        (into [component value] args)))}))

(defn build [data errors on-change]
  {:data data
   :errors  errors
   :on-change on-change
   :path []})

(defn conj-path [ctx id]
  (update ctx :path conj id))

(defn get-data [ctx]
  (get-in (:data ctx) (:path ctx)))

(defn reduce-data [-ctx f init]
  (reduce-kv
   (fn [acc k _]
     (let [ctx (conj-path -ctx k)]
       (f acc ctx)))
   init
   (get-data -ctx)))

(defn set-data [ctx value]
  (let [{:keys [data path on-change]} ctx
        new-data (assoc-in data path value)]
    (on-change new-data)))
