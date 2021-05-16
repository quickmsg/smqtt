## SMQTT是一款开源的MQTT消息代理Broker，

SMQTT基于Netty开发，底层采用Reactor3反应堆模型,支持单机部署，支持容器化部署，具备低延迟，高吞吐量，支持百万TCP连接，同时支持多种协议交互，是一款非常优秀的消息中间件！
## smqtt目前拥有的功能如下：

![架构图](icon/component.png)

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



## main方式启动

引入依赖
```markdown
<dependency>
  <groupId>io.github.quickmsg</groupId>
  <artifactId>smqtt-core</artifactId>
  <version>1.0.4</version>
</dependency>

```

阻塞式启动服务：

```markdown

 Bootstrap.builder()
       .port(8555)
       .websocketPort(8999)
       .options(channelOptionMap -> {})
       .ssl(false)
       .reactivePasswordAuth((U,P)->true)
       .sslContext(new SslContext("crt","key"))
       .isWebsocket(true)
       .wiretap(false)
       .httpOptions(Bootstrap.HttpOptions.builder().ssl(false).httpPort(62212).accessLog(true).build())
       .build()
       .startAwait();

```

非阻塞式启动服务：

```markdown

 
 Bootstrap bootstrap = 
        Bootstrap.builder()
       .port(8555)
       .websocketPort(8999)
       .options(channelOptionMap -> {})
       .ssl(false)
       .sslContext(new SslContext("crt","key"))
       .isWebsocket(true)
       .wiretap(false)
       .httpOptions(Bootstrap.HttpOptions.builder().ssl(false).httpPort(62212).accessLog(true).build())
       .build()
       .start().block();

assert bootstrap != null;
 // 关闭服务
 bootstrap.shutdown();

```


## jar方式


1. 下载源码 mvn compile package -P jar

```markdown
  在smqtt-bootstrap/target目录下生成jar
```

2. 准备配置文件 config.properties

```markdown
    
    # 开启tcp端口
    smqtt.tcp.port=1883
    # 高水位
    smqtt.tcp.lowWaterMark=4000000
    # 低水位
    smqtt.tcp.highWaterMark=80000000
    # 开启ssl加密
    smqtt.tcp.ssl=false
    # 证书crt smqtt.tcp.ssl.crt =
    # 证书key smqtt.tcp.ssl.key =
    # 开启日志
    smqtt.tcp.wiretap=false
    # boss线程
    smqtt.tcp.bossThreadSize=4;
    # work线程
    smqtt.tcp.workThreadSize=8;
    # websocket端口
    smqtt.websocket.port=8999;
    # websocket开启
    smqtt.websocket.enable=true;
    # smqtt用户
    smqtt.tcp.username=smqtt;
    # smqtt密码
    smqtt.tcp.password=smqtt;
    # 开启http
    smqtt.http.enable=true;
    # 开启http端口
    smqtt.http.port=1999;
    # 开启http日志
    smqtt.http.accesslog=true;
    # 开启ssl
    smqtt.http.ssl.enable=false;
    # smqtt.http.ssl.crt =;
    # smqtt.http.ssl.key;
    # 开启集群
    smqtt.cluster.enable=false
    # 集群节点地址
    smqtt.cluster.url=127.0.0.1:7771,127.0.0.1:7772
    # 节点端口
    smqtt.cluster.port=7771
    # 节点名称
    smqtt.cluster.node=node-1
  ```

3. 启动服务

```markdown
  java -jar smqtt-bootstrap-1.0.1-SNAPSHOT.jar <conf.properties路径>
```



## docker 方式


拉取镜像

``` 
# 拉取docker镜像地址
docker pull 1ssqq1lxr/smqtt:latest
```

启动镜像默认配置

``` 
# 启动服务
docker run -it  -p 1883:1883 1ssqq1lxr/smqtt
```

启动镜像使用自定义配置（ 准备配置文件conf.properties）


``` 
# 启动服务
docker run -it  -v <配置文件路径目录>:/conf -p 1883:1883  -p 1999:1999 1ssqq1lxr/smqtt
```


## 测试服务（启动http端口）

- 启动客户端订阅主题 test/+

- 使用http接口推送mqtt消息

``` 
# 推送消息
curl -H "Content-Type: application/json" -X POST -d '{"topic": "test/teus", "qos":2, "retain":true, "message":"我来测试保留消息3" }' "http://localhost:1999/smqtt/publish"
```



## wiki地址

集群类配置参考文档:

[smqtt文档](https://doc.smqtt.cc/)


## License

[Apache License, Version 2.0](LICENSE)



