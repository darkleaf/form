(ns darkleaf.form.common
  (:require
   [clojure.string :as string]
   [darkleaf.form.context :as ctx]))

(defn label-text [ctx]
  (let [path (ctx/get-path ctx)
        id (last path)]
    (-> id
        (name)
        (string/replace "-" " ")
        (string/capitalize))))

(defn error-text [ctx error]
  (str error))

(defn event->value [e]
  (.. e -target -value))

(defn input [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (merge opts
                          {:value value
                           :on-change #(set-value (event->value %))})]
    [:input input-opts]))

(defn textarea [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (merge opts
                          {:value value
                           :on-change #(set-value (event->value %))})]
    [:textarea input-opts]))

(defn- select-input [input-opts options]
  [:select input-opts
   (for [o options
         :let [value (first o)
               title (second o)]]
     [:option {:value value, :key value} title])])

(defn select [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        options (get opts :options [])
        input-opts (-> opts
                       (dissoc :options)
                       (merge {:value value
                               :on-change #(set-value (event->value %))}))]
    [select-input input-opts options]))

(defn- event->multi-select-value [e]
  (let [options (-> e
                    (.. -target -options)
                    (array-seq))]
    (->> options
         (filter #(.-selected %))
         (map #(.-value %))
         (doall))))

(defn multi-select [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        options (get opts :options [])
        input-opts (-> opts
                       (dissoc :options)
                       (merge {:multiple true
                               :value value
                               :on-change #(set-value (event->multi-select-value %))}))]
    [select-input input-opts options]))

(defn- event->checkbox-value [e]
  (.. e -target -checked))

(defn checkbox [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (merge opts
                          {:type :checkbox
                           :checked value
                           :on-change #(set-value (event->checkbox-value %))})]
    [:input input-opts]))
