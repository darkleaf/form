(ns darkleaf.form.keyword-params)

(defn- keyword-syntax? [s]
  (re-matches #"[A-Za-z*+!_?-][A-Za-z0-9*+!_?-]*" s))

(defn keyify-params [target]
  (cond
    (map? target)
    (into {}
          (for [[k v] target]
            [(if (and (string? k) (keyword-syntax? k))
               (keyword k)
               k)
             (keyify-params v)]))
    (vector? target)
    (vec (map keyify-params target))
    :else
    target))
