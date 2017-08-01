(ns darkleaf.form-test.utils.common-checks
  (:require
   [darkleaf.form.context :as ctx]
   [darkleaf.form-test.utils.render :as utils.render]
   [darkleaf.form-test.utils.events :as utils.events]
   [darkleaf.form-test.utils.blank  :as utils.blank]
   [clojure.string :as string]
   [cljs.test :as t :include-macros true]))

(defn plain-errors [element-builder data path-for-error]
  (let [cur-errors [:error-1 :error-2]
        errors     (assoc-in {} path-for-error {ctx/errors-key cur-errors})
        f          (ctx/build data errors utils.blank/update)
        el         (element-builder f)
        _          (utils.render/render el)
        html       (utils.render/get-html)]
    (t/is (every? #(string/includes? html %) cur-errors))))

(defn i18n-errors [element-builder data path-for-error]
  (let [cur-errors [:error-1 :error-2]
        errors     (assoc-in {} path-for-error {ctx/errors-key cur-errors})
        i18n-text  "some error"
        i18n-error (fn [path error]
                     (when (and
                            (= path path-for-error)
                            (= error (first cur-errors)))
                       i18n-text))
        f          (ctx/build data errors utils.blank/update {:error i18n-error})
        el         (element-builder f)
        _          (utils.render/render el)
        html       (utils.render/get-html)]
    (t/is (string/includes? html i18n-text))))

(defn plain-label [element-builder data given-label]
  (let [f    (ctx/build data utils.blank/errors utils.blank/update)
        el   (element-builder f)
        _    (utils.render/render el)
        html (utils.render/get-html)]
    (t/is (string/includes? html given-label))))

(defn i18n-label [element-builder data path-for-label]
  (let [i18n-text  "some label"
        i18n-label (fn [path]
                     (when (= path path-for-label)
                       i18n-text))
        f          (ctx/build data
                              utils.blank/errors
                              utils.blank/update
                              {:label i18n-label})
        el         (element-builder f)
        _          (utils.render/render el)
        html       (utils.render/get-html)]
    (t/is (string/includes? html i18n-text))))

(defn usual-input-change [element-builder data attr-path input-selector]
  (t/async
   done
   (let [value     (get-in data attr-path)
         new-value "new value"
         update    (fn [path f]
                     (t/is (= attr-path path))
                     (t/is (= new-value (f :smth)))
                     (done))
         f         (ctx/build data utils.blank/errors update)
         el        (element-builder f)
         _         (utils.render/render el)
         input     (utils.render/path-selector attr-path input-selector)]
     (t/is (not (= value new-value)))
     (utils.events/change input new-value))))

(defn checkbox-input-change [element-builder data attr-path]
  (t/async
   done
   (let [new-value (not (get-in data attr-path))
         update    (fn [path f]
                     (t/is (= attr-path path))
                     (t/is (= new-value (f :smth)))
                     (done))
         f         (ctx/build data utils.blank/errors update)
         el        (element-builder f)
         _         (utils.render/render el)
         input     (utils.render/path-selector attr-path "input")]
     (utils.events/change-checkbox input new-value))))

(defn multiselect-input-change [element-builder data attr-path new-value]
  (t/async
   done
   (let [value  (get-in data attr-path)
         update (fn [path f]
                  (t/is (= attr-path path))
                  (t/is (= new-value (f :smth)))
                  (done))
         f      (ctx/build data utils.blank/errors update)
         el     (element-builder f)
         _      (utils.render/render el)
         input  (utils.render/path-selector attr-path "select")]
     (t/is (not (= value new-value)))
     (utils.events/change-multiselect input new-value))))
