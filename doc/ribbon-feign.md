# feign & ribbon

## feign 基于接口契约的调用方式

### 定义契约接口

#### 定义契约接口

see [Say.java](../serviceApi/src/main/java/sample/ds/cn/api/useCloudWithFeign/Say.java)
~~~
@FeignClient(value = "demo.service", /*,path = "say"*/)
public interface Say {

    @RequestMapping("say/hello")
    public String sayHello(@RequestParam(value = "word") String word);


    @RequestMapping("say/hi")
    public String sayHi(@RequestParam(value = "word") String word);
}
~~~
* 用@FeignClient标记一个接口。
* 它要调用的服务是"demo.service"， *demo.service*即serviceProvider的spring.application.name。
* *path = "say"* 可以标记整个interface的url-mapping, 但是它不能被RestController继承。
* *@RequestMapping* 标记对应的url-mapping

#### 使用Gradle将包upload到nexus

在根的build.gradle已经配置好
~~~
uploadArchives {
        def nexusUrl = "http://nexus.ds.cn:8081/nexus/content/repositories/snapshots/"
        configuration = configurations.archives
        repositories.mavenDeployer {
            repository(url: nexusUrl) {
                authentication(userName: "deployment", password: "deployment")
            }
        }
    }
~~~
请自行更改域名nexus.ds.cn对应的ip地址

### serviceProvider 实现

加入依赖 
~~~
compile 'samples:serviceApi:1.0-SNAPSHOT'
~~~

在本例中，实现该接口的[SayImplController.java](../serviceProvider/src/main/java/sample/ds/cn/serviceController/usecloud/SayImplController.java) 如下：

~~~
@RestController
//    @RequestMapping("say/")  /* 假如上面开启了 path = "say"， 那么可以这样子用 */
//    public class SayImplController { /* 假如上面开启了 path = "say"， 那么要用这行 */
public class SayImplController implements Say {

    @Autowired
    private DiscoveryClient client;

    @Override
//    @RequestMapping("hello")
    public String sayHello(String word) {
        ServiceInstance instance = client.getLocalServiceInstance();
        return "hello " + word + ".\nhost:" + instance.getHost() + ", port:" + instance.getPort() + ", service_id:" + instance.getServiceId() + "";
    }

    @Override
//    @RequestMapping("hi")
    public String sayHi(String word) {
        ServiceInstance instance = client.getLocalServiceInstance();
        return "hi " + word + ".\nhost:" + instance.getHost() + ", port:" + instance.getPort() + ", service_id:" + instance.getServiceId() + "";
    }
}
~~~

可见它实际是个超简单的RestController, 与一般Spring MVC的RestController无异。<br>
实际也可以独立访问的这个RestController。
赋于它魔法的是服务启动类[Application.java](/Users/wu/github.mine/spring-cloud-with-dubbo/serviceProvider/src/main/java/sample/ds/cn/Application.java)

~~~
@SpringCloudApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
~~~

* @SpringCloudApplication = @SpringBootApplication + @EnableDiscoveryClient + @EnableCircuitBreaker
* @EnableDiscoveryClient =  读配置并注册本程序到eureka

## serviceConsumer 实现
加入依赖 
~~~
compile 'samples:serviceApi:1.0-SNAPSHOT'
~~~
启动类[ConsumerMain.java](../serviceConsumer/src/main/java/sample/ds/cn/ConsumerMain.java)开启Feign标注
~~~
@EnableFeignClients // 就是这个，它会扫描启动类当前及以下包的带@FeignClient的东西
@SpringCloudApplication

...

public class ConsumerMain extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(ConsumerMain.class, args);
        ContextUtil.setApplicationContext(applicationContext);
    }

    ...
~~~

以如下方式调用即可, 可参阅[FeignWithHytrixCommandSample.java](../serviceConsumer/src/main/java/sample/ds/cn/consumer/FeignWithHytrixCommandSample.java)
~~~
    @Autowired
    private Say say;

    public String helloWithoutFallback(String word) {

                Thread thread = Thread.currentThread();
                System.out.println("helloWithoutFallback:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return "Feign:" + say.sayHello(word);
    }
~~~    

## ribbon 基于http-rest-url的调用方式

### serviceProvider的实现

see [SayImplController.java](../serviceProvider/src/main/java/sample/ds/cn/serviceController/usecloud/SayImplController.java) 如下：

~~~
@RestController
@RequestMapping("say/") 
public class SayImplController { 

    @Autowired
    private DiscoveryClient client;

    @Override
    @RequestMapping("hello")
    public String sayHello(String word) {
        ServiceInstance instance = client.getLocalServiceInstance();
        return "hello " + word + ".\nhost:" + instance.getHost() + ", port:" + instance.getPort() + ", service_id:" + instance.getServiceId() + "";
    }

    @Override
    @RequestMapping("hi")
    public String sayHi(String word) {
        ServiceInstance instance = client.getLocalServiceInstance();
        return "hi " + word + ".\nhost:" + instance.getHost() + ", port:" + instance.getPort() + ", service_id:" + instance.getServiceId() + "";
    }
}
~~~
该类以url方式提供两个服务，一个say hello， 一个say hi。

### serviceConsumer 实现

启动类[ConsumerMain.java](../serviceConsumer/src/main/java/sample/ds/cn/ConsumerMain.java)注入RestTemplate

~~~
    @Bean
    @LoadBalanced  
        // see org/springframework/cloud/client/loadbalancer/LoadBalancerAutoConfiguration.java
        // line 80
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
~~~

调用[RestTemplateWithHytrixCommandSample.java](../serviceConsumer/src/main/java/sample/ds/cn/consumer/RestTemplateWithHytrixCommandSample.java)

~~~
    public String syncSayHi(@PathVariable String word) {
        String result = restTemplate.getForEntity("http://demo.service/say/hi?word=" + word, String.class).getBody();
        return "restTemplate: " + result;
    }
~~~
其中的*demo.service*即serviceProvider的spring.application.name。