(ns darkleaf.form.bootstrap4
  (:require
   [darkleaf.form.common :as common]
   [darkleaf.form.messages :as messages]
   [darkleaf.form.context :as ctx]))

(defn- add-class [classes class]
  (str classes " " class))

(defn errors [ctx]
  (let [errors (ctx/get-own-errors ctx)]
    [:div
     (for [error errors]
       ^{:key error}
       [:div.form-control-feedback
        (messages/error ctx error)])]))

(defn label [ctx]
  [:label.form-control-label (messages/label ctx)])

(defn- top-classes [ctx class]
  (let [errors (ctx/get-own-errors ctx)
        has-errors? (not-empty errors)]
    (str (if has-errors? "has-danger ")
         class)))

(defn input-wrapper [ctx class & xs]
  (let [classes (top-classes ctx class)]
    [common/input-wrapper ctx :div {:class classes} xs]))

(defn text [top-ctx id & {:keys [input]
                          :or {input {}}}]
  (let [ctx (ctx/nested top-ctx id)
        input-opts (-> {:type :text}
                       (merge input)
                       (update :class add-class "form-control"))]
    [input-wrapper ctx "form-group"
     [label ctx]
     [common/input ctx input-opts]
     [errors ctx]]))

(defn textarea [top-ctx id & {:keys [input]
                              :or {input {}}}]
  (let [ctx (ctx/nested top-ctx id)
        input-opts (update input :class add-class "form-control")]
    [input-wrapper ctx "form-group"
     [label ctx]
     [common/textarea ctx input-opts]
     [errors ctx]]))

(defn select [top-ctx id & {:keys [input options]
                            :or {input {}, options []}}]
  (let [ctx (ctx/nested top-ctx id)
        input-opts (update input :class add-class "form-control custom-select")]
    [input-wrapper ctx "form-group"
     [label ctx]
     [common/select ctx options input-opts]
     [errors ctx]]))

(defn multi-select [top-ctx id & {:keys [input options]
                                  :or {input {}, options []}}]
  (let [ctx (ctx/nested top-ctx id)
        input-opts (update input :class add-class "form-control")]
    [input-wrapper ctx "form-group"
     [label ctx]
     [common/multi-select ctx options input-opts]
     [errors ctx]]))

(defn checkbox [top-ctx id & {:keys [input]}]
  (let [ctx (ctx/nested top-ctx id)
        input-opts (update input :class add-class "custom-control-input")]
    [input-wrapper ctx "form-check"
     [:label.custom-control.custom-checkbox
      [common/checkbox ctx input-opts]
      [:span.custom-control-indicator]
      [:span.custom-control-description (messages/label ctx)]]
     [errors ctx]]))

(defn radio-select [top-ctx id & {:keys [options]
                                  :or {options []}}]
  (let [ctx (ctx/nested top-ctx id)]
    [input-wrapper ctx "form-group"
     [label ctx]
     (for [o options
           :let [value (first o)
                 title (second o)]]
       [:div.custom-controls-stacked {:key value}
        [:label.custom-control.custom-radio
         [common/radio ctx {:class "custom-control-input"
                            :value value}]
         [:span.custom-control-indicator]
         [:span.custom-control-description title]]])
     [errors ctx]]))

;; ctx должен быть вышестоящий, элемент не может удалить себя сам
(defn remove-nested-btn [top-ctx idx & {:as opts}]
  (let [input-opts (-> opts
                       (update :class add-class "btn")
                       (merge {:tag :button}))]
    [common/remove-nested top-ctx idx input-opts]))

(defn add-nested-btn [top-ctx builder & {:as opts}]
  (let [input-opts (-> opts
                       (update :class add-class "btn")
                       (merge {:tag :button}))]
    [common/add-nested top-ctx builder input-opts]))

(defn error-alerts [ctx]
  (let [errors (ctx/get-own-errors ctx)]
    [:div
     (for [error errors]
       ^{:key error}
       [:div.alert.alert-danger
        (messages/error ctx error)])]))
