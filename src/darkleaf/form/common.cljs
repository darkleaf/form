(ns darkleaf.form.common
  (:require
   [darkleaf.form.event :as e]
   [darkleaf.form.context :as ctx]))

(defn input-wrapper [ctx tag opts rest]
  (let [input-opts (merge opts
                          {:data-path (-> ctx ctx/get-path str)})]
    (into [tag input-opts] rest)))

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

(defn- select-input [options input-opts]
  [:select input-opts
   (for [o options
         :let [value (first o)
               title (second o)]]
     [:option {:value value, :key value} title])])

(defn select [ctx options opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (merge opts {:value value
                                :on-change #(-> % e/value set-value)})]
    [select-input options input-opts]))

(defn multi-select [ctx options opts]
  (let [value (ctx/get-data ctx)
        set-value #(ctx/set-data ctx %)
        input-opts (-> opts
                       (merge {:multiple true
                               :value value
                               :on-change #(-> % e/multi-select-value set-value)}))]
    [select-input options input-opts]))

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
        input-opts (merge opts
                          {:type :radio
                           :checked (= original-value input-value)
                           :on-change #(-> % e/value set-value)})]
    [:input input-opts]))

(defn- remove-by-idx [coll pos]
  (vec
   (concat
    (subvec coll 0 pos)
    (subvec coll (inc pos)))))

(defn remove-nested [ctx idx opts]
  (let [tag (get opts :tag :div)
        text (get opts :text "remove")
        on-click (fn [] (ctx/update-data ctx #(remove-by-idx % idx)))
        input-opts (-> opts
                       (dissoc :tag :text)
                       (merge {:on-click on-click}))]
    [tag input-opts text]))

(defn add-nested [ctx builder opts]
  (let [tag (get opts :tag :div)
        text (get opts :text "add new")
        new-item (builder)
        on-click (fn [] (ctx/update-data ctx #(conj % new-item)))
        input-opts (-> opts
                       (dissoc :tag :text)
                       (merge {:on-click on-click}))]
    [tag input-opts text]))
