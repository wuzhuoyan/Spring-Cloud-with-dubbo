# SpringCloudSample

## 模块划分 

1. eureka 注册中心，提供服务发现
2. serviceConsumer 服务消费者例子 （主看入口SampleController，Hystrix(javanica https://github.com/Netflix/Hystrix/tree/master/hystrix-contrib/hystrix-javanica)及ribbon、feign、dubbo的示范均在本工程）
3. serviceProvider 服务提供者例子
4. turbine Hystrix的看板
5. zuul api网关例子
6. api 一个接口声明定义的工程

其中serviceConsumer + api + serviceProvider即与我们当前的webapp + dubbo (api + service) 结构十分相似



## 关于使用这个sample

### 关于eureka

#### 配置
注意本示范项目内的eureka-server是三节点的示范集群，其分别为
~~~
# master
server.port=1111
eureka.instance.hostname=eureka.master
eureka.client.serviceUrl.defaultZone=http://eureka.backup:1112/eureka/,http://eureka.third:1113/eureka/

# backup
server.port=1112
eureka.instance.hostname=eureka.backup
eureka.client.serviceUrl.defaultZone=http://eureka.master:1111/eureka/,http://eureka.third:1113/eureka/

# third
server.port=1113
eureka.instance.hostname=eureka.third
eureka.client.serviceUrl.defaultZone=http://eureka.master:1111/eureka/,http://eureka.backup:1112/eureka/
~~~
我在笔记本装了个docker，上面跑着三个节点。
其etc/hosts文件是这样子的
~~~
172.17.0.3	eureka.master
172.17.0.5	eureka.backup
172.17.0.6	eureka.third
~~~
假如想在单独的计算机上面运行，请参照上述设置即可。
当然你也可以用我笔记本上的三个已经部署好的eureka节点，但是不保证ip地址不经常换噢。

#### 运行
项目使用gradle, 使用gradle bootRepackage，可以生成可执行jar包
使用如下命令启动你的eureka-server
~~~
/**
 * 启动方式
 * java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node1
 * java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node2
 * java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node3
 */
~~~
当然也可以只启动其中一个，这不妨碍使用。

### 其他配置
在eureka-server外的其他项目，我为了切换ip方便，均写成了这样
~~~
eureka:
  client:
    service-url:
      defaultZone: http://mine.ds.cn:1111/eureka/,...
~~~
当你在本地机器执行的时候，可以像以下那样设置一下你本地的域名与ip的映射
~~~
10.8.71.217	mine.ds.cn 
~~~

### 关于看板

单点看板，请参考sample.ds.cn.ConsumerMain
~~~
/*打开监控面板 locahost:port/hystrix， 输入 locahost:8762/hystrix.stream，2000， title等 */
@EnableHystrixDashboard
~~~

多点集群看板， 请参考turbine工程

看板的各种数字含义，参考
https://github.com/Netflix/Hystrix/wiki/Dashboard