## ![image](icon/logo.png) SMQTT是一款开源的MQTT消息代理Broker，

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
12. 持久化支持（session 保留消息）




## main方式启动

引入依赖
```markdown
<dependency>
  <groupId>io.github.quickmsg</groupId>
  <artifactId>smqtt-core</artifactId>
  <version>1.0.5</version>
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
# smqtt.cluster.external.port

# 数据库配置(选配)
db.driverClassName=com.mysql.jdbc.Driver
db.url=jdbc:mysql://127.0.0.1:3306/smqtt?characterEncoding=utf-8&useSSL=false&useInformationSchema=true&serverTimezone=UTC
db.username=root
db.password=123
# 连接池初始化连接数
db.initialSize=10
# 连接池中最多支持多少个活动会话
db.maxActive=300
# 向连接池中请求连接时,超过maxWait的值后,认为本次请求失败
db.maxWait=60000
# 回收空闲连接时，将保证至少有minIdle个连接
db.minIdle=2
# redis配置(选配)
# 单机模式：single 哨兵模式：sentinel 集群模式：cluster
redis.mode=single
# 数据库
redis.database=0
# 密码
redis.password=
# 超时时间
redis.timeout=3000
# 最小空闲数
redis.pool.min.idle=8
# 连接超时时间(毫秒)
redis.pool.conn.timeout=3000
# 连接池大小
redis.pool.size=10
# 单机配置
redis.single.address=127.0.0.1:6379
# 集群配置
redis.cluster.scan.interval=1000
redis.cluster.nodes=127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005
redis.cluster.read.mode=SLAVE
redis.cluster.retry.attempts=3
redis.cluster.slave.connection.pool.size=64
redis.cluster.master.connection.pool.size=64
redis.cluster.retry.interval=1500
# 哨兵配置
redis.sentinel.master=mymaster
redis.sentinel.nodes=127.0.0.1:26379,127.0.0.1:26379,127.0.0.1:26379
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


## License

[Apache License, Version 2.0](https://github.com/quickmsg/smqtt/blob/main/LICENSE)


## 麻烦关注下公众号！
![image](icon/icon.jpg)

- 添加微信号`Lemon877164954`，拉入smqtt官方交流群
- 加入qq群 `700152283` 


