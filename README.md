[![Build Status](https://travis-ci.org/darkleaf/form.svg?branch=master)](https://travis-ci.org/darkleaf/form)
[![Coverage Status](https://coveralls.io/repos/github/darkleaf/form/badge.svg)](https://coveralls.io/github/darkleaf/form)
[![Clojars Project](https://img.shields.io/clojars/v/darkleaf/form.svg)](https://clojars.org/darkleaf/form)

# Features

+ twitter bootstrap4
+ nested forms support
+ interface for i18n engines
+ interface for validation engines
+ [clojure.spec integration](src/darkleaf/form/spec_integration.cljs)
+ complete test coverage

# Demo

+ [demo](https://darkleaf.github.io/form/)
+ [demo source](test/darkleaf/form_test/demo.cljs)

# Installation

```clojure
[reagent "0.7.0"]
[darkelaf/form "0.1.0-SNAPSHOT"]
```

# Usage example

```html
<!doctype html>
<html>
  <head>
    <title>Form</title>

    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />

    <!-- add bootstrap styles -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous" />
  </head>
  <body>
    <div class="container">
      <div id="point">app mount point</div>
    </div>
    <script type="text/javascript" src="build/dev/main.js"></script>
  </body>
</html>
```

```clojure
(ns app
  (:require
   [reagent.core :as r]
   [darkleaf.form.context :as ctx]
   [darkleaf.form.bootstrap4 :as bootstrap]))

(defn i18n-error [path error]
  "some logic")

(defn i18n-label [path]
  "some logic")

(defn form [data errors]
  ;; i18n is optional
  (let [f (ctx/build data errors update {:error i18n-error, :label i18n-label})]
    [:form
     [bootstrap/text f :some-attribute-name]]))

(def data {:some-attrubte-name "foo bar"})
(def errors {})

(r/render [form data errors]
          (.getElementById js/document "point"))
```

Please see demo or tests for more examples.