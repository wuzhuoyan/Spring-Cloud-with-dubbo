package sample.ds.cn;


import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import sample.ds.cn.common.ContextUtil;

import java.util.concurrent.TimeUnit;

/*
* Importing additional configuration classes
* You don’t need to put all your @Configuration into a single class.
* The @Import annotation can be used to import additional configuration classes.
* Alternatively, you can use @ComponentScan to automatically pick up all Spring components,
* including @Configuration classes.
*
* Importing XML configuration
* If you absolutely must use XML based configuration,
* we recommend that you still start with a @Configuration class.
* You can then use an additional @ImportResource annotation to load XML configuration files.
*/
//@Configuration
//@Import({MyFilterConfig.class})

/*
* Using a root package also allows the @ComponentScan annotation to be used without needing to specify a basePackage attribute.
* You can also use the @SpringBootApplication annotation if your main class is in the root package.
*/
//@ComponentScan(basePackages = {"myproject.controller", "myproject.filter", "myproject.domain"})
//@ImportResource(locations = {"file:/Users/wu/Dropbox/dev/springbootTest/src/main/resouces/application_no_scan_bean.xml"})
//@ImportResource(locations = {"classpath:application_no_scan_bean.xml"})

/*
* This annotation tells Spring Boot to “guess” how you will want to configure Spring,
* based on the jar dependencies that you have added. Since spring-boot-starter-web added Tomcat and Spring MVC,
* the auto-configuration will assume that you are developing a web application and setup Spring accordingly.
*/
//@EnableAutoConfiguration

/* same as @Configuration @EnableAutoConfiguration @ComponentScan */
//@SpringBootApplication

/* Spring Cloud */
//@EnableDiscoveryClient
@EnableFeignClients
//@EnableCircuitBreaker

/* @SpringCloudApplication =  @SpringBootApplication + @EnableDiscoveryClient + @EnableCircuitBreaker */
@SpringCloudApplication

/*使用 @EnableCircuitBreaker 或者 @EnableHystrix 表明Spring boot工程启用hystrix,两个注解是等价的.*/
//@EnableHystrix

/*打开监控面板 locahost:port/hystrix， 输入 locahost:8762/hystrix.stream，2000， title等 */
@EnableHystrixDashboard
public class ConsumerMain extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(ConsumerMain.class, args);
        ContextUtil.setApplicationContext(applicationContext);
    }

    @Bean
    @LoadBalanced
        // see org/springframework/cloud/client/loadbalancer/LoadBalancerAutoConfiguration.java
        // line 80
    RestTemplate restTemplate() {
        return new RestTemplate();
    }


     /* Create a deployable war file needed */
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(ConsumerMain.class);
//    }

    /*
    * 使用编程式的JAVA容器配置,
    * JettyEmbeddedServletContainerFactory 可以替换成:
    * TomcatEmbeddedServletContainerFactory
    * UndertowEmbeddedServletContainerFactory
    */
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
////        factory.setPort(9000);
//        factory.setSessionTimeout(10, TimeUnit.MINUTES);
//        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));
//        return factory;
//    }

    /* JSP-config*/
//    @Bean
//    InternalResourceViewResolver internalResourceViewResolver () {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/jsp/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }


    /* Thrift */
//    private void
}