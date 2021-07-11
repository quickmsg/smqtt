---
sort: 3
---
# websocket文档

{% include list.liquid %}

# 启动webSocket


```
 Bootstrap bootstrap = Bootstrap.builder()
                .websocketPort(8999)
                .isWebsocket(true)
                .build()
                .start().block();
```