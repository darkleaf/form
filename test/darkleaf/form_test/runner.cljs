(ns darkleaf.form-test.runner
  (:require
   [jx.reporter.karma :refer-macros [run-all-tests]]
   [darkleaf.form-test.bootstrap4.checkbox-test]
   [darkleaf.form-test.bootstrap4.multi-select-test]
   [darkleaf.form-test.bootstrap4.radio-select-test]
   [darkleaf.form-test.bootstrap4.select-test]
   [darkleaf.form-test.bootstrap4.text-test]
   [darkleaf.form-test.bootstrap4.textarea-test]
   [darkleaf.form-test.bootstrap4.nested-vector-test]
   [darkleaf.form-test.bootstrap4.nested-map-test]
   [darkleaf.form-test.bootstrap4.remove-nested-test]
   [darkleaf.form-test.bootstrap4.add-nested-test]))

(enable-console-print!)

(defn ^:export run [karma]
  (run-all-tests karma))
