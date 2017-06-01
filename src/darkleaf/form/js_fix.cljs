(ns darkleaf.form.js-fix
  (:require
   [goog.object :as obj]
   [cljsjs.react]
   [cljsjs.react.dom]))

(obj/set js/global "React" js/window.React)
(obj/set js/global "ReactDOM" js/window.ReactDOM)
