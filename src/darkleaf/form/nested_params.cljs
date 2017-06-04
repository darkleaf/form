(ns darkleaf.form.nested-params
  "From macchiato.middleware.nested-params")

(defn assoc-conj
  "Associate a key with a value in a map. If the key already exists in the map,
  a vector of values is associated with the key."
  [map key val]
  (assoc map key
             (if-let [cur (get map key)]
               (if (vector? cur)
                 (conj cur val)
                 [cur val])
               val)))

(defn- parse-nested-keys
  "Parse a parameter name into a list of keys using a 'C'-like index
  notation.
  For example:
    \"foo[bar][][baz]\"
    => [\"foo\" \"bar\" \"\" \"baz\"]"
  [param-name]
  (let [[_ k ks] (re-find #"^([\s\S]*?)((?:\[[\s\S]*?\])*)$" (name param-name))
        keys     (if ks (map second (re-seq #"\[(.*?)\]" ks)))]
    (cons k keys)))

(defn- assoc-vec [m k v]
  (let [m (if (contains? m k) m (assoc m k []))]
    (assoc-conj m k v)))

(defn- assoc-nested
  "Similar to assoc-in, but treats values of blank keys as elements in a
  list."
  [m [k & ks] v]
  (if k
    (if ks
      (let [[j & js] ks]
        (if (= j "")
          (assoc-vec m k (assoc-nested {} js v))
          (assoc m k (assoc-nested (get m k {}) ks v))))
      (if (map? m)
        (assoc-conj m k v)
        {k v}))
    v))

(defn- param-pairs
  "Return a list of name-value pairs for a parameter map."
  [params]
  (mapcat
    (fn [[name value]]
      (if (and (sequential? value) (not (coll? (first value))))
        (for [v value] [name v])
        [[name value]]))
    params))

(defn nest-params
  "Takes a flat map of parameters and turns it into a nested map of
  parameters, using the function parse to split the parameter names
  into keys."
  [params]
  (reduce
    (fn [m [k v]]
      (assoc-nested m (parse-nested-keys k) v))
    {}
    (param-pairs params)))
