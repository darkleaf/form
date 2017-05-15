FROM clojure:boot-2.7.1-alpine

RUN apk --no-cache add nodejs=6.9.5-r0
RUN npm install -g \
    jsdom@10.1.0 \
    websocket@1.0.24
ENV NODE_PATH=/usr/lib/node_modules
