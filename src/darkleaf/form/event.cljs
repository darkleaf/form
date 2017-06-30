(ns darkleaf.form.event)

(defn value [e]
  (.. e -target -value))

(defn multi-select-value [e]
  (let [options (-> e
                    (.. -target -options)
                    (array-seq))
        selected (filter #(.-selected %) options)
        values (map #(.-value %) selected)]
    (doall values)))

(defn checkbox-value [e]
  (.. e -target -checked))
