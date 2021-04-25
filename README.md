![image](icon/smqtt.jpg)

## SMQTT是一款开源的MQTT消息代理Broker，

SMQTT基于Netty开发，底层采用Reactor3反应堆模型,支持单机部署，支持容器化部署，
### smqtt目前拥有的功能如下：

1.  消息质量等级实现(支持qos0，qos1，qos2)
2.  会话消息
3.  保留消息
4.  遗嘱消息
5.  客户端认证
6.  tls加密
7.  websocket协议支持
8.  http协议交互
9.  SPI接口扩展支持
    - 消息管理接口（会话消息/保留消息管理）
    - 通道管理接口 (管理系统的客户端连接)
    - 认证接口 （用于自定义外部认证）
    - 拦截器  （用户自定义拦截消息）
10. 集群支持（gossip协议实现）
11. 容器化支持 


### 后面规划项目

1. 规则引擎
2. Web管理系统
3. 监控系统
4. 协议桥接agent（用户其他协议与broker之间交互）


## 快速开始





docker镜像地址
``` 
docker pull 1ssqq1lxr/smqtt:latest
```

启动服务(默认1883端口)

``` 
docker run -it  -p  1883:1883 -e wiretap=true 1ssqq1lxr/smqtt

修改端口使用 -e port =1884 -p 1883:1884

```

### 压测报告

