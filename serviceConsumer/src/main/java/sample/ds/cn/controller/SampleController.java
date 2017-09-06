package sample.ds.cn.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Observer;
import sample.ds.cn.consumer.DubboConsumerSample;
import sample.ds.cn.consumer.FeignWithHytrixCommandSample;
import sample.ds.cn.consumer.RestTemplateWithHytrixCommandSample;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by wu on 17/8/2.
 */
@RestController
@RequestMapping("/say")
public class SampleController {

    /*Feign with HytrixCommand */

    @Autowired
    FeignWithHytrixCommandSample feignWithHytrixCommandSample;

    @RequestMapping(value = "/helloWithoutFallback/{word}")
    public String helloWithoutFallback(@PathVariable String word) {
//                Thread thread = Thread.currentThread();
//                System.out.println("helloWithoutFallback:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return feignWithHytrixCommandSample.helloWithoutFallback(word);
    }

    @RequestMapping(value = "/helloWithFallback/{word}")
    public String helloWithFallback(@PathVariable String word) throws Exception {
//                Thread thread = Thread.currentThread();
//                System.out.println("helloWithFallback:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return feignWithHytrixCommandSample.helloWithFallback(word);
    }

    /* Resttemplate with HytrixCommand (use javanica)*/
    /* 参考 https://github.com/Netflix/Hystrix/tree/master/hystrix-contrib/hystrix-javanica */

    @Autowired
    RestTemplateWithHytrixCommandSample restTemplateWithHytrixCommandSample;

    /* Synchronous Execution with RestTemplate, use ribbon */
    @RequestMapping(value = "/sync/{word}")
    public String sync(@PathVariable String word) {
//        Thread thread = Thread.currentThread();
//        System.out.println("sync:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return restTemplateWithHytrixCommandSample.syncSayHi(word);
    }

    /* Asynchronous Execution with RestTemplate, use ribbon */
    /*  注意：@HystrixCommand标记的asyncSayHi方法假如与当前方法async在同一个类, 死活无法future.get
        类似问题报告,https://stackoverflow.com/questions/38429349/hysterix-javanica-asyncresult-future-get-throwing-exception
     */
    @RequestMapping(value = "/async/{word}")
    public String async(@PathVariable String word) throws ExecutionException, InterruptedException {
//        Thread thread = Thread.currentThread();
//        System.out.println("async:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        Future<String> f = restTemplateWithHytrixCommandSample.asyncSayHi(word);
//        System.out.println("async:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return f.get(); // get的时候才触发调用
    }

    /* Reactive Execution */
    @RequestMapping(value = "/reactive/{word}")
    public String reactive(@PathVariable String word){
//        Thread thread = Thread.currentThread();
//        System.out.println("reactive:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());

        Observable<String> observable = restTemplateWithHytrixCommandSample.reactiveSayHi(word); //EAGER模式在这里就触发了调用

//        System.out.println("reactive:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());

        observable.subscribe(new Observer<String>() { //LAZY模式在这一行触发调用

            @Override
            public void onCompleted() {
                Thread thread = Thread.currentThread();
                System.out.println("completed:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onNext(String str) {
                Thread thread = Thread.currentThread();
                System.out.println("onNext:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
                System.out.println(str);
            }
        });

//        System.out.println("reactive:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
//        System.out.println("success");
        return "success";
    }


    /* call dubbo */
    @Autowired
    DubboConsumerSample dubboConsumerSample;

    /* Generic mode, no dependices */
    @RequestMapping(value = "/callDubboBaseGeneric/")
    public String callDubboBaseGeneric(){
        Object o =  dubboConsumerSample.callDubboService("activityServiceWithGeneric",
                "findActivityApplyDetailed",
                new String[]{"java.lang.String"},
                new Object[]{"58dcbe68a294435b49ee093f"});
        return JSONObject.toJSONString(o);
    }

    /* Interface mode, dependices */
    @RequestMapping(value = "/callDubboBaseInterface/")
    public String callDubboBaseInterface() throws Exception {
        Object model =  dubboConsumerSample.findActivityApplyDetailed("58dcbe68a294435b49ee093f");
        return JSONObject.toJSONString(model);
    }

}
