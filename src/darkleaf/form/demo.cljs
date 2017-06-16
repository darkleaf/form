(ns darkleaf.form.demo
  (:require
   [cljs.spec.alpha :as s]
   [clojure.string :as string]
   [reagent.core :as r]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.spec-integration :refer [explain-data->errors]]
   [darkleaf.form.bootstrap4 :as bootstrap]))

(enable-console-print!)


(s/def ::string string?)
(s/def ::present-string (s/and string? #(not (string/blank? %))))
(s/def ::email #(re-matches #"^\S+@\S+$" %))
(s/def ::password #(< 8 (count %)))

(s/def :root/example-text ::present-string)
(s/def :root/example-password (s/and ::present-string ::password))
(s/def :root/example-textarea ::string)

(s/def ::data (s/keys :req [:root/example-text
                            :root/example-password
                            :root/example-textarea]))

(def initial-data
  {:root/example-text "Some text"
   :root/example-password "Some password"
   :root/example-textarea "Some\nbig\ntext"
   :root/example-select "second"
   :root/example-multipleselect []})

(defn component []
  (let [data (r/atom initial-data)]
    (fn []
      (let [errors (->> @data (s/explain-data ::data) (explain-data->errors))
            f (ctx/build @data errors #(reset! data %))]
        [:form
         [bootstrap/text f :root/example-text]
         [bootstrap/text f :root/example-password :type :password]
         [bootstrap/textarea f :root/example-textarea :rows 5]
         [bootstrap/select f :root/example-select :options [["first" "First"]
                                                            ["second" "Second"]]]
         [bootstrap/multi-select f :root/example-multipleselect
          :options [["first" "First"]
                    ["second" "Second"]]]

         [:p (str @data)]]))))

(r/render [component]
          (.getElementById js/document "point"))




;; (defmulti translate-error (fn [error path] [error path]))

;; (defmethod translate-error :default [error path]
;;   (if (not-empty path)
;;     (translate-error error (pop path))
;;     error))
