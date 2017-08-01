(ns darkleaf.form-test.utils.blank
  (:refer-clojure :exclude [update]))

(def ^:private update (constantly nil))
(def ^:private errors {})
