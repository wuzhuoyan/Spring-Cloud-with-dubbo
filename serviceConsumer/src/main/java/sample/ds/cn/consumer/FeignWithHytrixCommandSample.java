package sample.ds.cn.consumer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.ds.cn.api.useCloudWithFeign.Say;

import java.util.Date;

/**
 * Created by wu on 17/8/17.
 */
@Service
public class FeignWithHytrixCommandSample {

    /*Feign*/
    @Autowired
    private Say say;

    public String helloWithoutFallback(String word) {

                Thread thread = Thread.currentThread();
                System.out.println("helloWithoutFallback:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return "Feign:" + say.sayHello(word);
    }


    @HystrixCommand(
            commandKey = "FeignWithHytrixCommandSample#helloWithFallback",
            fallbackMethod = "sayHelloFallBack")
    public String helloWithFallback(String word) throws Exception {

                Thread thread = Thread.currentThread();
                System.out.println("helloWithFallback:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return "Feign:" + say.sayHello(word);
    }

    private String sayHelloFallBack(String word){
        return "fallback:" + word;
    }
}
