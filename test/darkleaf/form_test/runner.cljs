(ns darkleaf.form-test.runner
  (:require
   [jx.reporter.karma :refer-macros [run-tests run-all-tests]]
   [darkleaf.form-test.meta-test]))

(enable-console-print!)

(defn ^:export run [karma]
  (run-all-tests karma))
