---
sort: 0
---
# 启动


## main方式启动

-  引入依赖
```markdown
<dependency>
  <groupId>io.github.quickmsg</groupId>
  <artifactId>smqtt-core</artifactId>
  <version>1.0.3</version>
</dependency>
```

- 阻塞式启动服务:

```markdown

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
       .startAwait();
```

- 非阻塞式启动服务：
    
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
## 源码打包jar启动


### 下载源码 

```markdown
  mvn compile package <smqtt-bootstrap module> -P jar
```

在smqtt-bootstrap/target目录下生成jar

### 使用配置文件 config.properties

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
  ```

### 不实用配置文件
使用jvm启动参数:

```markdown
  java -jar -Dsmqtt.tcp.port=1883 -Dsmqtt.http.enable=true  smqtt-bootstrap-1.0.1-SNAPSHOT.jar
```
### 启动服务

```markdown
  java -jar smqtt-bootstrap-1.0.1-SNAPSHOT.jar <conf.properties路径>
```

## Docker启动


### 拉取镜像

``` 
# 拉取docker镜像地址
docker pull 1ssqq1lxr/smqtt:latest
```

### 使用环境变量启动

``` 
docker run -it -e smqtt.tcp.port=1883  -e smqtt.http.enable=true   -p 1883:1883 1ssqq1lxr/smqtt
```

### 使用配置文件启动
- 准备配置文件conf.properties
- 启动服务


``` 
docker run -it  -v <配置文件路径目录>:/conf -p <宿主机port>:<配置文件port>  1ssqq1lxr/smqtt
```







{% include list.liquid %}