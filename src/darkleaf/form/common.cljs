(ns darkleaf.form.common
  (:require
   [darkleaf.form.event :as e]
   [darkleaf.form.context :as ctx]))

(defn input [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (merge opts
                          {:value value
                           :on-change #(-> % e/value set-value)})]
    [:input input-opts]))

(defn textarea [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (merge opts
                          {:value value
                           :on-change #(-> % e/value set-value)})]
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
                               :on-change #(-> % e/value set-value)}))]
    [select-input input-opts options]))

(defn multi-select [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        options (get opts :options [])
        input-opts (-> opts
                       (dissoc :options)
                       (merge {:multiple true
                               :value value
                               :on-change #(-> % e/multi-select-value set-value)}))]
    [select-input input-opts options]))

(defn checkbox [ctx opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (merge opts
                          {:type :checkbox
                           :checked value
                           :on-change #(-> % e/checkbox-value set-value)})]
    [:input input-opts]))

(defn radio [ctx opts]
  (let [original-value (ctx/get-data ctx)
        input-value (:value opts)
        set-value #(ctx/set-data ctx %)
        input-name (ctx/get-str-path ctx)
        input-opts (merge opts
                          {:type :radio
                           :name input-name
                           :checked (= original-value input-value)
                           :on-change #(-> % e/value set-value)})]
    [:input input-opts]))
