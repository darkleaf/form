(ns darkleaf.form.context
  (:require
   [goog.object :as gobj]
   [reagent.core :as r]
   [clojure.string :as string]))

(def errors-key ::errors)

(defprotocol Protocol
  (nested [this k])
  (get-data [this])
  (get-own-errors [this])
  (get-errors-subtree [this])
  (get-external-opts [this])
  (get-path [this])
  (update-data [this f]))

(defn set-data [ctx val]
  (update-data ctx (fn [_old] val)))

(defn get-str-path [ctx]
  (let [path (get-path ctx)]
    (string/join " " path)))

(deftype Type [path data errors update external-opts]
  Protocol
  (get-data [_]
    data)

  (get-own-errors [_]
    (get errors errors-key '()))

  (get-errors-subtree [_]
    errors)

  (get-external-opts [_]
    external-opts)

  (get-path [_]
    path)

  (update-data [_ f]
    (update path f))

  (nested [_ k]
    (Type. (conj path k)
           (get data k)
           (get errors k)
           update
           external-opts))

  IEquiv
  (-equiv [this other]
    (and
     (= (get-data this)
        (get-data other))
     (= (get-errors-subtree this)
        (get-errors-subtree other))))

  ISeqable
  (-seq [this]
    (reduce-kv
     (fn [acc k _]
       (conj acc [k (nested this k)]))
     []
     (get-data this))))

(defn build
  ([data errors update external-opts]
   (Type. cljs.core/PersistentQueue.EMPTY
          data
          errors
          update
          external-opts))
  ([data errors update]
   (build data errors update {})))
