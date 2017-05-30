(ns darkleaf.form.context
  (:refer-clojure :exclude [map]))

(defprotocol Protocol
  (get-data [ctx])
  (get-errors [ctx])
  (get-name [ctx])
  (conj-path [ctx id])
  (map [ctx f]))

(defrecord Record [path data errors name-prefix]
  Protocol
  (get-data [_]
    (get-in data path))
  (get-errors [_]
    (get errors path []))
  (get-name [_]
    (reduce
     (fn [full-name id]
       (let [suffix (cond
                      (keyword? id) (str "[" (name id) "]")
                      (integer? id) "[]")]
         (str full-name suffix)))
     name-prefix
     path))

  (conj-path [this id]
    (update this :path conj id))
  (map [this f]
    (map-indexed
     (fn [i _]
       (let [ctx (conj-path this i)]
         (f ctx)))
     (get-data this))))

(defn build [data errors name-prefix]
  (->Record [] data errors name-prefix))
