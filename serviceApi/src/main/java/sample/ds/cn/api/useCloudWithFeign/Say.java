package sample.ds.cn.api.useCloudWithFeign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wu on 17/8/1.
 */

@FeignClient(value = "demo.service", fallback = SayFallback.class /*,path = "say"*/)
// commandkey , see https://segmentfault.com/a/1190000009849932
public interface Say {

    @RequestMapping("say/hello")
    public String sayHello(@RequestParam(value = "word") String word);


    @RequestMapping("say/hi")
    public String sayHi(@RequestParam(value = "word") String word);
}
