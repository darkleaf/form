(ns darkleaf.form-test.meta-test
  (:require
   [cljs.test :as t :include-macros true]
   [darkleaf.form-test.bootstrap4.checkbox-test]
   [darkleaf.form-test.bootstrap4.multi-select-test]
   [darkleaf.form-test.bootstrap4.radio-select-test]
   [darkleaf.form-test.bootstrap4.select-test]
   [darkleaf.form-test.bootstrap4.text-test]
   [darkleaf.form-test.bootstrap4.textarea-test]))

(defn run-all []
  (t/run-all-tests))
