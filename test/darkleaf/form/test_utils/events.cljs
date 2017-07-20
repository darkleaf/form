(ns darkleaf.form.test-utils.events
  (:require
   [cljsjs.react]))

(defn change [input value]
  (js/React.addons.TestUtils.Simulate.change
   input
   (clj->js {:target {:value value}})))
