(ns darkleaf.form-test.utils.events
  (:require
   [cljsjs.react]))

(defn change [input value]
  (js/React.addons.TestUtils.Simulate.change
   input
   (clj->js {:target {:value value}})))

(defn change-checkbox [input state]
  (set! (.-checked input) state)
  (js/React.addons.TestUtils.Simulate.change input))

(defn change-multiselect [input value]
  (let [js-options (.querySelectorAll input "option")
        options (array-seq js-options)]
    (doseq [opt options
            :let [opt-value (.-value opt)
                  selected? (some #(= opt-value %) value)]]
      (set! (.-selected opt) selected?))
    (js/React.addons.TestUtils.Simulate.change input)))

(defn click [element]
  (js/React.addons.TestUtils.Simulate.click element))
