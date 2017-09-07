# spring-cloud-with-dubbo

## 用来做什么？(What does it do?)

这是一个Spring Cloud的示范工程。包括如何使用Eureka、Ribbon及Feign、Hystrix、Zuul等组件，及如何合并使用Dubbo。
在当前这个版本，并不包括上述组件以外的其他示范。<br>
想了解更多，可以参阅 [Spring Cloud 官方指南](http://cloud.spring.io/spring-cloud-static/Camden.SR7/)<br>

假如一开始就使用Spring CLoud, 或者你的工程是全新的，那么本工程较能体现它的参考价值。<br>
如果你想从旧有的dubbo-soa或dubbo-client-mapp改造使用，那么建议参考另外一个示范工程
[dubbo-with-spring-cloud](https://github.com/wuzhuoyan/dubbo-with-spring-cloud)

## 关于Spring Cloud版本 (About Version)

Spring Cloud 是一个快速迭代的活跃项目，为了在碰到问题时能通过多种渠道获得解决方案，也因为作者过去使用的习惯版本的原因，
这个示范工程选用Camden.SR3。

### 整体依赖

请参阅[build.gradle](build.gradle)<br>
请自行更改仓库域名nexus.ds.cn对应的ip地址

### 工程依赖关系及用途 (Dependices & Do what)

请参阅[settings.gradle](settings.gradle), 及以下描述

* eureka（注册中心）
* serviceApi（基于接口的服务契约定义）
* serviceConsumer 依赖于serviceApi（服务消费者）
* serviceProvider 依赖于serviceApi（服务提供者）
* turbine（Hystrix的监视看板）
* zuul（边界、网关）

## 快速开始 (Hello world)

### 使用现成的临时的Eureka注册中心

现在即有现成的、临时的、可使用的Eureka集群，它们就运行于我桌面笔记本的Docker里面。<br>
将以下配置加入你的Hosts文件里面。即可在不更改任何配置的情况下使用示范工程的服务提供者(serviceProvider)和消费者(serviceConsumer)
~~~
10.8.71.82	mine.ds.cn # IP地址如不能使用，请随时联系架构组Wu
~~~

### 打包 serviceProvider 并运行

进入serviceProvider目录下，执行gradle bootRepackage.
~~~
cd serviceProvider;
gradle bootRepackage; 
cd build/libs;
java -jar serviceProvider-1.0-SNAPSHOT.jar --spring.profiles.active=node1;
java -jar serviceProvider-1.0-SNAPSHOT.jar --spring.profiles.active=node2;
~~~
现在，你有两个节点的服务提供者了。当然你也可以只执行node1或node2。<br>
注意：<br>
当没有指定"--spring.profiles.active"参数时，
默认取resources下面的application.yml或者application.properties，而serviceProvider下并没有它们之一。
所以在这里必须指定一个"--spring.profiles.active"参数。

### 打包 进入serviceConsumer目录下 并运行

进入serviceConsumer目录下，执行gradle bootRepackage。
~~~
cd serviceConsumer;
gradle bootRepackage; 
cd build/libs;
java -jar serviceConsumer-1.0-SNAPSHOT.jar; 
~~~
现在，你有一个消费者了。其实你也可以使用"--spring.profiles.active"参数增加多个消费者。这在测试Zuul时比较有用。<br>
如:
~~~
java -jar serviceConsumer-1.0-SNAPSHOT.jar --spring.profiles.active=c1;
java -jar serviceConsumer-1.0-SNAPSHOT.jar --spring.profiles.active=c2;
~~~

### 测试

访问：[http://127.0.0.1:5678/say/sync/world](http://127.0.0.1:5678/say/sync/world)<br>
即得到以下结果：<br>
restTemplate: hi world.<br>
host:10.8.71.30, port:9003, service_id:demo.service


## 相对深入的主题

1. [Eureka](eureka/README.md)
3. [Ribbon](doc/ribbon-feign.md)
4. [Feign](doc/ribbon-feign.md)
5. [Hystrix(Javanica-Hystrix)](doc/hystrix.md)
6. Turbine
7. Zuul


