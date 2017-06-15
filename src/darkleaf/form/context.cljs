(ns darkleaf.form.context
  (:require
   [goog.object :as gobj]
   [reagent.core :as r]))

(defprotocol Protocol
  (nested [this k])
  (get-data [this])
  (update-data [this f]))

(defn set-data [ctx val]
  (update-data ctx (fn [_old] val)))

(deftype Type [path data on-change]
  Protocol
  (get-data [_]
    (get-in data path))

  (update-data [_ f]
    (let [new (update-in data path f)]
      (on-change new)))

  (nested [_ k]
    (Type. (conj path k) data on-change))

  IEquiv
  (-equiv [this other]
    (and
     (= (get-data this)
        (get-data other))))

  ISeqable
  (-seq [this]
    (reduce-kv
     (fn [acc k _]
       (conj acc [k (nested this k)]))
     []
     (get-data this))))

(defn build [data errors on-change]
  (Type. [] data on-change))
