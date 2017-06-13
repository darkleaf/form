(ns darkleaf.form.context
  (:require
   [goog.object :as gobj]
   [rum.core :as rum]))

(def ^:private ctx-key "ctx")

(def ^:private provider-mixin
  {:child-context
   (fn [state]
     (let [[value & _] (:rum/args state)]
       {ctx-key (constantly value)})) ;; hack

   :class-properties
   {:childContextTypes {ctx-key js/React.PropTypes.any.isRequired}}})

(rum/defc provider < provider-mixin
  [value child]
  child)

(def ^:private receiver-mixin
  {:class-properties
   {:contextTypes {ctx-key js/React.PropTypes.any.isRequired}}})

(rum/defcc receiver < receiver-mixin
  [this callback]
  (let [value ((gobj/getValueByKeys this "context" ctx-key))] ;; hack
    (callback value)))

(defn build [data errors on-change]
  {:data data
   :errors  errors
   :on-change on-change
   :path []})

(defn conj-path [ctx id]
  (update ctx :path conj id))

(defn get-id [ctx]
  (last (:path ctx)))

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
