package sample.ds.cn.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 启动方式
 * java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node1
 * java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node2
 * java -jar eureka-sample-0.0.1-SNAPSHOT.jar --spring.profiles.active=node3
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}
}
