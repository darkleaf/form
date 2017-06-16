(ns darkleaf.form.bootstrap4
  (:require
   [clojure.string :as string]
   [goog.object :as gobj]
   [darkleaf.form.context :as ctx]))

(defn- class-names [& names]
  (->> names
       (remove nil?)
       (string/join " ")))

(defn- event->value [e]
  (gobj/getValueByKeys e "target" "value"))

(defn build-input [display-name constructor]
  (with-meta
    (fn input [top-ctx id & {:as opts}]
      (let [ctx (ctx/nested top-ctx id)
            value (ctx/get-data ctx)
            set-value #(ctx/set-data ctx %)
            errors (ctx/get-errors ctx)
            has-errors? (not-empty errors)]
        [:div {:class (class-names
                       "form-group"
                       (if has-errors? "has-danger"))}
         [:label.form-control-label id]
         (constructor value set-value opts)
         (for [error errors]
           ^{:key error} [:div.form-control-feedback (str error)])]))
    {:display-name display-name}))

(def text
  (build-input
   "BootstrapInput"
   (fn [value set-value -opts]
     (let [opts (merge {:type :text}
                       -opts
                       {:value value
                        :on-change #(set-value (event->value %))})]
       [:input.form-control opts]))))

(def textarea
  (build-input
   "BootstrapTextarea"
   (fn [value set-value -opts]
     (let [opts (merge -opts
                       {:value value
                        :on-change #(set-value (event->value %))})]
       [:textarea.form-control opts]))))

(def select
  (build-input
   "BootstrapSelect"
   (fn [value set-value -opts]
     (let [options (get -opts :options [])
           opts (-> -opts
                    (dissoc :options)
                    (merge {:value value
                            :on-change #(set-value (event->value %))}))]

       [:select.form-control opts
        (for [o options
              :let [value (first o)
                    title (second o)]]
          [:option {:value value, :key value} title])]))))

(defn- event->multi-select-value [e]
  (let [options (-> e
                    (gobj/getValueByKeys "target" "options")
                    (array-seq))]
    (->> options
         (filter #(.-selected %))
         (map #(.-value %))
         (doall))))

(def multi-select
  (build-input
   "BootstrapMultiSelect"
   (fn [value set-value -opts]
     (let [options (get -opts :options [])
           opts (-> -opts
                    (dissoc :options)
                    (merge {:multiple true
                            :value value
                            :on-change #(set-value (event->multi-select-value %))}))]

       [:select.form-control opts
        (for [o options
              :let [value (first o)
                    title (second o)]]
          [:option {:value value, :key value} title])]))))
