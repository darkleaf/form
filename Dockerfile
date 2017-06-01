FROM clojure:boot-2.7.1-alpine

RUN apk --no-cache add nodejs=6.9.5-r0
RUN npm install -g \
    jsdom@10.1.0 \
    jsdom-global@3.0.2

RUN echo \
      $'#!/bin/sh \n \
        node -r /usr/lib/node_modules/jsdom-global/register $@' \
    > /usr/local/bin/node_dom \
    && chmod +x /usr/local/bin/node_dom

# cider repl fix
RUN mkdir -p /Users/m_kuzmin/projects/github \
    && ln -s /usr/src/app /Users/m_kuzmin/projects/github/form
