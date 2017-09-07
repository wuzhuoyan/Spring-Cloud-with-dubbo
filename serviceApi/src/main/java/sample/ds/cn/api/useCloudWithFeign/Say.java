package sample.ds.cn.api.useCloudWithFeign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wu on 17/8/1.
 */

@FeignClient(value = "demo.service", fallback = SayFallback.class /*,path = "say"*/)
/*
commandkey是怎么组装设置的? see https://segmentfault.com/a/1190000009849932

Feign.configKey in feign-core-9.3.1-sources.jar!/feign/Feign.java

public static String configKey(Class targetType, Method method) {
    StringBuilder builder = new StringBuilder();
    builder.append(targetType.getSimpleName());
    builder.append('#').append(method.getName()).append('(');
    for (Type param : method.getGenericParameterTypes()) {
      param = Types.resolve(targetType, targetType, param);
      builder.append(Types.getRawType(param).getSimpleName()).append(',');
    }
    if (method.getParameterTypes().length > 0) {
      builder.deleteCharAt(builder.length() - 1);
    }
    return builder.append(')').toString();
  }

    commandKey的构造，这里组装了类、方法名、参数，比如本文的实例，commandKey=RemoteProductService#getProduct(int)
 */
public interface Say {

    @RequestMapping("say/hello")
    public String sayHello(@RequestParam(value = "word") String word);


    @RequestMapping("say/hi")
    public String sayHi(@RequestParam(value = "word") String word);
}
