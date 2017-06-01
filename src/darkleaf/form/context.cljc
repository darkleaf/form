(ns darkleaf.form.context
  (:refer-clojure :exclude [reduce])
  (:require
   [clojure.core :as clj]))

(defprotocol Protocol
  (get-data [ctx])
  (get-errors [ctx])
  (get-name [ctx])
  (conj-path [ctx id])
  (reduce [ctx f init]))

(defrecord Record [path data errors name-prefix]
  Protocol
  (get-data [_]
    (get-in data path))
  (get-errors [_]
    (get errors path []))
  (get-name [_]
    (clj/reduce
     (fn [full-name id]
       (let [suffix (cond
                      (keyword? id) (str "[" (name id) "]")
                      (integer? id) "[]")]
         (str full-name suffix)))
     name-prefix
     path))

  (conj-path [this id]
    (update this :path conj id))
  (reduce [this f init]
    (reduce-kv
     (fn [acc k _]
       (let [ctx (conj-path this k)]
         (f acc ctx)))
     init
     (get-data this))))

(defn build [data errors name-prefix]
  (->Record [] data errors name-prefix))
