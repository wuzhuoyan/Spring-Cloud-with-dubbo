package sample.ds.cn.serviceController.usecloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RestController;
import sample.ds.cn.api.useCloudWithFeign.Say;
//import sample.ds.cn.api.usecloud.Say;

/**
 * Created by wu on 17/8/1.
 */
@RestController
//@RequestMapping("say/")
public class SayImplController implements Say {
//    public class SayImplController {

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
