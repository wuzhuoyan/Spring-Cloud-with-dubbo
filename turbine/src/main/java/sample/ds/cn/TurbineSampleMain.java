package sample.ds.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * Created by wu on 17/8/15.
 */
@SpringCloudApplication
@EnableHystrixDashboard
@EnableTurbine

/*打开监控面板 locahost:port/hystrix， 输入 locahost:port/turbine.stream，2000， title等 */
public class TurbineSampleMain {

    public static void main(String[] args) {
        SpringApplication.run(TurbineSampleMain.class, args);
    }
}
