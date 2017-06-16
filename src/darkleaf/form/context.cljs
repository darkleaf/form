(ns darkleaf.form.context
  (:require
   [goog.object :as gobj]
   [reagent.core :as r]))

(def errors-key ::errors)

(defprotocol Protocol
  (nested [this k])
  (get-data [this])
  (get-errors [this])
  (update-data [this f]))

(defn set-data [ctx val]
  (update-data ctx (fn [_old] val)))

(deftype Type [path data errors on-change]
  Protocol
  (get-data [_]
    (get-in data path))

  (get-errors [_]
    (let [value-path (conj path errors-key)]
      (get-in errors value-path '())))

  (update-data [_ f]
    (let [new (update-in data path f)]
      (on-change new)))

  (nested [_ k]
    (Type. (conj path k) data errors on-change))

  IEquiv
  (-equiv [this other]
    (and
     (= (get-data this)
        (get-data other))
     (= (get-errors this)
        (get-errors other))))

  ISeqable
  (-seq [this]
    (reduce-kv
     (fn [acc k _]
       (conj acc [k (nested this k)]))
     []
     (get-data this))))

(defn build [data errors on-change]
  (Type. cljs.core/PersistentQueue.EMPTY
         data
         errors
         on-change))
