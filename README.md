## ![image](icon/logo.png) SMQTT是一款开源的MQTT消息代理Broker，

SMQTT基于Netty开发，底层采用Reactor3反应堆模型,支持单机部署，支持容器化部署，具备低延迟，高吞吐量，支持百万TCP连接，同时支持多种协议交互，是一款非常优秀的消息中间件！
## smqtt目前拥有的功能如下：
![架构图](icon/component.png)

1.  消息质量等级实现(支持qos0，qos1，qos2)
2.  topicFilter支持
    - topic分级（test/test）
    - +支持（单层匹配）
    - #支持（多层匹配）
2.  会话消息
    - 默认内存存储
    - 支持持久化（redis/db）
3.  保留消息
     - 默认内存存储
     - 支持持久化（redis/db）
     > 需要下载源码自行打包
4.  遗嘱消息
     > 设备掉线时候触发
5.  客户端认证
     - 支持spi注入外部认证
6.  tls加密
     - 支持tls加密（mqtt端口/http端口）
7.  websocket协议支持
     > 使用websocket协议包装mqtt协议
8.  http协议交互
    - 支持http接口推送消息
    - 支持spi扩展http接口
9.  SPI接口扩展支持
    - 消息管理接口（会话消息/保留消息管理）
    - 通道管理接口 (管理系统的客户端连接)
    - 认证接口 （用于自定义外部认证）
    - 拦截器  （用户自定义拦截消息）
10. 集群支持（gossip协议实现）
11. 容器化支持 
    > 默认镜像最新tag: 1ssqq1lxr/smqtt
12. 持久化支持（session 保留消息）
13. 管理后台
    > 请参考smqtt文档如何启动管理后台
   

## 尝试一下

> 大家不要恶意链接，谢谢！

|  管理   | 说明  | 其他  |
|  ----  | ----  |----  |
| 123.57.69.210:1883  | mqtt端口 |用户名：smqtt 密码：smqtt |
| 123.57.69.210:8999  | mqtt over websocket |用户名：smqtt 密码：smqtt  |
| http://123.57.69.210:60000/smqtt/admin | 管理后台 |用户名：smqtt 密码：smqtt  |


## main方式启动

引入依赖
```markdown
<!--smqtt依赖 -->
<dependency>
  <groupId>io.github.quickmsg</groupId>
  <artifactId>smqtt-core</artifactId>
  <version>${Latest version}</version>
</dependency>
<!--集群依赖 -->
<dependency>
   <artifactId>smqtt-registry-scube</artifactId>
   <groupId>io.github.quickmsg</groupId>
   <version>${Latest version}</version>
</dependency>
<!--管理ui依赖 -->
<dependency>
   <artifactId>smqtt-ui</artifactId>
   <groupId>io.github.quickmsg</groupId>
   <version>${Latest version}</version>
</dependency>

```

阻塞式启动服务：

```markdown

Bootstrap.builder()
          .rootLevel(Level.INFO)
          .wiretap(false)
          .port(8555)
          .websocketPort(8999)
          .options(channelOptionMap -> { })//netty options设置
          .childOptions(channelOptionMap -> { }) //netty childOptions设置
          .highWaterMark(1000000)
          .reactivePasswordAuth((U, P) -> true)
          .lowWaterMark(1000)
          .ssl(false)
          .sslContext(new SslContext("crt", "key"))
          .isWebsocket(true)
          .httpOptions(Bootstrap.HttpOptions.builder().enableAdmin(true).ssl(false).accessLog(true).build())
          .clusterConfig(
               ClusterConfig.builder()
                                .clustered(false)
                                .port(7773)
                                .nodeName("node-2")
                                .clusterUrl("127.0.0.1:7771,127.0.0.1:7772")
                                .build()
           )
           .started(bootstrap->{})
           .build()
           .startAwait();

```

非阻塞式启动服务：

```markdown

 
Bootstrap bootstrap = Bootstrap.builder()
          .rootLevel(Level.INFO)
          .wiretap(false)
          .port(8555)
          .websocketPort(8999)
          .options(channelOptionMap -> { })//netty options设置
          .childOptions(channelOptionMap -> { }) //netty childOptions设置
          .highWaterMark(1000000)
          .reactivePasswordAuth((U, P) -> true)
          .lowWaterMark(1000)
          .ssl(false)
          .sslContext(new SslContext("crt", "key"))
          .isWebsocket(true)
          .httpOptions(Bootstrap.HttpOptions.builder().enableAdmin(true).ssl(false).accessLog(true).build())
          .clusterConfig(
               ClusterConfig.builder()
                                .clustered(false)
                                .port(7773)
                                .nodeName("node-2")
                                .clusterUrl("127.0.0.1:7771,127.0.0.1:7772")
                                .build()
           )
           .started(bootstrap->{})
           .build()
           .start().block();

```


## jar方式


1. 下载源码 mvn compile package -Dmaven.test.skip=true -P jar,web

```markdown
  在smqtt-bootstrap/target目录下生成jar
```

2. 准备配置文件 config.properties

```markdown
    
# 日志级别 ALL｜TRACE｜DEBUG｜INFO｜WARN｜ERROR｜OFF
smqtt.log.level=INFO
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
smqtt.tcp.bossThreadSize=4
# work线程
smqtt.tcp.workThreadSize=8
# websocket端口
smqtt.websocket.port=8999
# websocket开启
smqtt.websocket.enable=true
# smqtt用户
smqtt.tcp.username=smqtt
# smqtt密码
smqtt.tcp.password=smqtt
# 开启http
smqtt.http.enable=true
# 开启http日志
smqtt.http.accesslog=true
# 开启ssl
smqtt.http.ssl.enable=false
# smqtt.http.ssl.crt =
# smqtt.http.ssl.key =
# 开启管理后台（必须开启http）
smqtt.http.admin.enable=true
# 管理后台登录用户
smqtt.http.admin.username=smqtt
# 管理后台登录密码
smqtt.http.admin.password=smqtt
# 开启集群
smqtt.cluster.enable=false
# 集群节点地址
smqtt.cluster.url=127.0.0.1:7771,127.0.0.1:7772
# 节点端口
smqtt.cluster.port=7771
# 节点名称
smqtt.cluster.node=node-1
# 容器集群映射主机
# smqtt.cluster.external.host = localhost
# 容器集群映射port
smqtt.cluster.external.port

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

启动镜像使用自定义配置（同上准备配置文件conf.properties）


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

## 管理后台（60000端口）

### 如何开启

    
- main启动
    
   设置httpOptions && enableAdmin = true
   
    ``` 
    Bootstrap.httpOptions(Bootstrap.HttpOptions.builder().enableAdmin(true).ssl(false).accessLog(true).build())
  
    ```
- jar / docker 启动
    
   设置config.properties
   
    ``` 
    # 开启http
    smqtt.http.enable=true
    # 开启http日志
    smqtt.http.accesslog=true
    # 开启ssl
    smqtt.http.ssl.enable=false
    # smqtt.http.ssl.crt =
    # smqtt.http.ssl.key =
    # 开启管理后台（必须开启http）
    smqtt.http.admin.enable=true
    # 管理后台登录用户
    smqtt.http.admin.username=smqtt
    # 管理后台登录密码
    smqtt.http.admin.password=smqtt  
    ```


### 页面预览

![image](icon/admin.png)

## 压测文档
[点这里](https://blog.csdn.net/JingleYe/article/details/118190935)

## wiki地址

集群类配置参考文档:

[smqtt文档](https://quickmsg.github.io/smqtt)

## 注意事项

> 如果你引入了 spring-boot-starter-parent 依赖 请确保 版本>2.4.5 ，如果无法修改版本请手动添加以下以来

```markdown
  <dependency>
            <groupId>io.projectreactor.netty</groupId>
            <artifactId>reactor-netty</artifactId>
            <version>1.0.6</version>
        </dependency>
  <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.4.5</version>
  </dependency>

```
## License

[Apache License, Version 2.0](https://github.com/quickmsg/smqtt/blob/main/LICENSE)


## 麻烦关注下公众号！
![image](icon/icon.jpg)

- 添加微信号`Lemon877164954`，拉入smqtt官方交流群
- 加入qq群 `700152283` 


