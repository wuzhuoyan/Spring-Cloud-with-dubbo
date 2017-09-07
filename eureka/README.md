# eureka

## 依赖
增加子项目自有依赖
~~~
dependencies {
    compile('org.springframework.cloud:spring-cloud-starter-eureka-server')
}
~~~

## 编码
~~~
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}
}
~~~

## 打包 eureka 并 运行

进入eureka目录下，执行gradle bootRepackage.
~~~
cd eureka;
gradle bootRepackage;
cd build/libs;
java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node1;
java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node2;
java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node3;
~~~

## 关于配置
注意本示范项目内的eureka-server是三节点的示范集群，在三个application-xxxxx.properties文件里面分别为
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
我装了个docker，上面跑着三个节点。 其etc/hosts文件是这样子的：
~~~
172.17.0.3	eureka.master 
172.17.0.5	eureka.backup 
172.17.0.6	eureka.third
~~~
假如想在自己的计算机上面运行，请参照上述设置将ip改为127.0.0.1即可。<br>
 当然你也可以用我已经部署好的eureka节点。即在你的Hosts文件加入
~~~
10.8.71.82	mine.ds.cn
~~~
就可以了。那么你就不需要重新部署Eureka节点了。<br><br>
当然你也可以只启动其中一个节点，那也是没有问题的。

## 常用参数说明

以[application-node1.properties](main/resource/application-node1.properties)为例：<br>

see [eureka配置及集群参考资料](https://github.com/spring-cloud/spring-cloud-netflix/blob/a7398842078319dcaa353a708c12bb7b9fa85a4e/docs/src/main/asciidoc/spring-cloud-netflix.adoc#peer-awareness)

* spring.application.name=eureka.node # 应用程序名，也是注册在eureka中的应用程序名
* server.port=1111 # 使用什么端口开启服务
* eureka.instance.hostname=eureka.master # 主机名 not need, In fact, the eureka.instance.hostname is not needed if you are running on a machine that knows its own hostname (it is looked up using java.net.InetAddress by default).
* eureka.client.register-with-eureka=true # 是否注册自己 default true
* eureka.client.fetch-registry=true ＃ 是否获取服务list default true
* eureka.instance.preferIpAddress=true # 使用ip地址而非hostName


