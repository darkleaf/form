(ns darkleaf.form.test-runner
  (:require
   [jx.reporter.karma :refer-macros [run-tests run-all-tests]]
   [darkleaf.form.bootstrap4.checkbox-test]
   [darkleaf.form.bootstrap4.multi-select-test]
   [darkleaf.form.bootstrap4.radio-select-test]
   [darkleaf.form.bootstrap4.select-test]
   [darkleaf.form.bootstrap4.text-test]
   [darkleaf.form.bootstrap4.textarea-test]))

(enable-console-print!)

(defn ^:export run [karma]
  (run-all-tests karma))
