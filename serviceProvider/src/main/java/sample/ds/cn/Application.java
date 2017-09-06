package sample.ds.cn;


import org.springframework.boot.*;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;

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
//@EnableCircuitBreaker

/* @SpringCloudApplication =  @SpringBootApplication + @EnableDiscoveryClient + @EnableCircuitBreaker */
@SpringCloudApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

     /* Create a deployable war file needed */
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(Application.class);
//    }

    /*
    * 使用编程式的JAVA容器配置,
    * JettyEmbeddedServletContainerFactory 可以替换成:
    * TomcatEmbeddedServletContainerFactory
    * UndertowEmbeddedServletContainerFactory
    */
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
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