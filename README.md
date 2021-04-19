![image](icon/smqtt.jpg)

## SMQTT是一款开源的MQTT消息代理Broker，

### smqtt拥有的功能如下

1.  消息质量等级实现(支持qos0，qos1，qos2)
2.  会话消息
3.  保留消息
4.  遗嘱消息
5.  客户端认证
6.  tls加密
7.  websocket协议支持
8.  http协议交互
9.  SPI接口扩展支持
10. 集群支持
11. docker支持


### docker启动

docker镜像地址
``` 
docker pull 1ssqq1lxr/smqtt:latest
```

启动服务(默认1883端口)

``` 
docker run -it  -p  1883:1883 -e wiretap=true 1ssqq1lxr/smqtt

修改端口使用 -e port =1884 -p 1883:1884

```
