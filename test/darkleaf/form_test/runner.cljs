(ns darkleaf.form-test.runner
  (:require
   [jx.reporter.karma :refer-macros [run-tests run-all-tests]]
   [darkleaf.form-test.bootstrap4.checkbox-test]
   [darkleaf.form-test.bootstrap4.multi-select-test]
   [darkleaf.form-test.bootstrap4.radio-select-test]
   [darkleaf.form-test.bootstrap4.select-test]
   [darkleaf.form-test.bootstrap4.text-test]
   [darkleaf.form-test.bootstrap4.textarea-test]))

(enable-console-print!)

(defn ^:export run [karma]
  (run-all-tests karma))
