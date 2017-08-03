[![Build Status](https://travis-ci.org/darkleaf/form.svg?branch=master)](https://travis-ci.org/darkleaf/form)
[![Coverage Status](https://coveralls.io/repos/github/darkleaf/form/badge.svg)](https://coveralls.io/github/darkleaf/form)
[![Clojars Project](https://img.shields.io/clojars/v/darkleaf/form.svg)](https://clojars.org/darkleaf/form)

+ [demo](https://darkleaf.github.io/form/)
+ [demo source](test/darkleaf/form_test/demo.cljs)


# Installation

```
[reagent "0.7.0"]
[darkelaf/form "0.1.0-SNAPSHOT"]
```

Я так и не понял, как при разработке использовать cljsjs/react-with-addons,
а в jar прописывать зависимость от cljsjs/react.
Насколько я понял, у cljsjs есть планы совсем октазаться от react-with-addons.
Так что, пока нужно явно указать зависимость от reagent в своем проекте.
