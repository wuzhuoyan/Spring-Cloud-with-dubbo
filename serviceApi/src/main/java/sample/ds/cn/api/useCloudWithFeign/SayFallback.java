package sample.ds.cn.api.useCloudWithFeign;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wu on 17/8/1.
 */
@Component
public class SayFallback implements Say{
    @Override
    public String sayHello(@RequestParam String word) {
        return "sayHello fallback";
    }

    @Override
    public String sayHi(@RequestParam String word) {
        return "sayHi fallback";
    }
}
