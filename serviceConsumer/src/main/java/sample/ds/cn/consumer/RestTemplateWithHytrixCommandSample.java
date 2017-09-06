package sample.ds.cn.consumer;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * Created by wu on 17/8/17.
 */
@Service
@DefaultProperties(groupKey = "RestTemplateWithHytrixCommandSample"//,
//        threadPoolKey = "",
//        commandProperties = {},
//        threadPoolProperties = {},
//        ignoreExceptions = {}
        )
public class RestTemplateWithHytrixCommandSample {

    @Autowired
    RestTemplate restTemplate;

    /* Synchronous Execution */
    /* 参考 https://github.com/Netflix/Hystrix/tree/master/hystrix-contrib/hystrix-javanica */
    @HystrixCommand(
            /* groupKey = "", */ // 表示所属的group，一个group共用线程池, 默认值：getClass().getSimpleName();
            /* commandKey = "", */ // 默认值：当前执行方法名
            /* What thread-pool this command should run in (if running on a separate thread).
                        threadpool doesn't have a global override, only instance level makes sense */
            /* threadPoolKey = "" */
            commandProperties = { // 参见 HystrixCommandProperties 类

                    /* metrics */

                    /* milliseconds back that will be tracked
                    *  default => statisticalWindow: 10000 = 10 seconds (and default of 10 buckets so each bucket is 1 second)*/
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
                    /* number of buckets in the statisticalWindow
                    *  default => statisticalWindowBuckets: 10 = 10 buckets in a 10 second window so each bucket is 1 second */
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
                    /* Whether monitoring should be enabled (SLA and Tracers).  */
                    @HystrixProperty(name = "metrics.rollingPercentile.enabled", value = "true"),
                    /* number of milliseconds that will be tracked in RollingPercentile ,
                    *  default to 1 minute for RollingPercentile */
                    @HystrixProperty(name = "metrics.rollingPercentile.timeInMilliseconds", value = "60000"),
                    /* number of buckets percentileWindow will be divided into
                    *  default to 6 buckets (10 seconds each in 60 second window) */
                    @HystrixProperty(name = "metrics.rollingPercentile.numBuckets", value = "6"),
                    /* how many values will be stored in each percentileWindowBucket
                    *  default to 100 values max per bucket */
                    @HystrixProperty(name = "metrics.rollingPercentile.bucketSize", value = "100"),
                    /* time between health snapshots
                    *  default to 500ms as max frequency between allowing snapshots of health (error percentage etc) */
                    @HystrixProperty(name = "metrics.healthSnapshot.intervalInMilliseconds", value = "500"),


                    /* circuit breaker */

                    /* Whether circuit breaker should be enabled. default true */
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    /*  number of requests that must be made within a statisticalWindow before open/close decisions are made using stats
                        default 20 */
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
                    /* milliseconds after tripping circuit before allowing retry
                        default 5000 */
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                    /* % of 'marks' that must be failed to trip the circuit,
                        default 50(%) */
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    /* a property to allow forcing the circuit open (stopping all requests)
                        default false
                     */
                    @HystrixProperty(name = "circuitBreaker.forceOpen", value = "false"),
                    /* a property to allow ignoring errors and therefore never trip 'open' (ie. allow all traffic through)
                        default false
                     */
                    @HystrixProperty(name = "circuitBreaker.forceClosed", value = "false"),


                    /* execution */

                    /* Whether a command should be executed in a separate thread or not.  default THREAD, or SEMAPHORE */
                    /* SEMAPHORE: 当只想做并发控制，或调用shixiao的本地方法时可考虑使用
                                  参考 1. com.netflix.hystrix.AbstractCommand, line 1598, TryableSemaphore
                                  2. Semaphore that only supports tryAcquire and never blocks
                                  3. https://github.com/Netflix/Hystrix/wiki/Configuration#thread-or-semaphore
                                  4. https://github.com/Netflix/Hystrix/wiki/How-To-Use#common-patterns*/
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),

                    /* folling use THREAD*/
                    /* Whether timeout should be triggered default true */
                    @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
                    /* Timeout value in milliseconds for a command default 10000, 同时也适用于信号量方式，虽然它的名字比较poorly */
                    /* 关于丑陋名字的内涵，可以参考 */
                    /* 参考 https://groups.google.com/forum/#!topic/hystrixoss/8-x4kxWTHTE*/
                    /* 参考 https://github.com/Netflix/Hystrix/issues/673*/
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
                    /* Whether an underlying Future/Thread (when runInSeparateThread == true) should be interrupted after a timeout
                        default true */
                    @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true"),
                    /* Whether canceling an underlying Future/Thread (when runInSeparateThread == true) should interrupt the execution thread
                        default false
                        com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager
                        没有这个参数 execution.isolation.thread.interruptOnFutureCancel
                        打开即执行报错 ，但是实际com.netflix.hystrix。HystrixCommandProperties是有的，于是暂就只能用默认false囖
                    */
//                    @HystrixProperty(name = "execution.isolation.thread.interruptOnFutureCancel", value = "false"),

                    /* folling use SEMAPHORE*/
                    /* Number of permits for execution semaphore default 10 */
//                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10"),
                    /* Number of permits for fallback semaphore default 10 */
//                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "10"),


                    /* others */

                    /* Whether fallback should be attempted. default true */
                    @HystrixProperty(name = "fallback.enabled", value = "true"),
                    /* Whether request caching is enabled. default true, test use false */
                    /* 生命周期为request scope*/
                    @HystrixProperty(name = "requestCache.enabled", value = "true"),
                    /* Whether request caching is enabled. default true */
                    @HystrixProperty(name = "requestLog.enabled", value = "true")


            },
            threadPoolProperties = { //  参考 HystrixThreadPoolProperties 类
                    /* size of thread pool , default 10 */
                    @HystrixProperty(name = "coreSize", value = "10"),
                    /* minutes to keep a thread alive (though in practice this doesn't get used as by default we set a fixed size)
                        default 1 */
//                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    /* size of queue (this can't be dynamically changed so we use 'queueSizeRejectionThreshold' to artificially limit and reject)
                        default -1 turns if off and makes us use SynchronousQueue */
//                    @HystrixProperty(name = "maxQueueSize", value = "101"),
                    /* number of items in queue , default 5 */
//                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),

                    /* milliseconds for rolling number default 10000 */
//                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
                    /* number of buckets in rolling number (10 means 1-second buckets) */
//                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10")

            },
            fallbackMethod = "sayHiFallBack"
    )
    public String syncSayHi(@PathVariable String word) {
//        Thread thread = Thread.currentThread();
//        System.out.println("syncSayHi:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        String result = restTemplate.getForEntity("http://demo.service/say/hi?word=" + word, String.class).getBody();
        return "restTemplate: " + result;
    }

    /* Asynchronous Execution */
    @HystrixCommand(fallbackMethod = "sayHiFallBack")
    public  Future<String> asyncSayHi(final String word) {
//                Thread thread = Thread.currentThread();
//                System.out.println("asyncSayHi:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                String result = restTemplate.getForEntity("http://demo.service/say/hi?word=" + word, String.class).getBody();
//                Thread thread = Thread.currentThread();
//                System.out.println("asyncSayHi:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
                return "restTemplate: " + result;
            }
        };
    }

    /* Reactive Execution */
    @HystrixCommand(
            /* default */observableExecutionMode = ObservableExecutionMode.EAGER, // default EAGER or LAZY
            fallbackMethod = "sayHiFallBack")
    public Observable<String> reactiveSayHi(final String word) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
//                        Thread thread = Thread.currentThread();
//                        System.out.println("reactiveSayHi:"+ thread.getName() + ":" + thread.getId() + ":" + new Date().getTime());
                        String result = restTemplate.getForEntity("http://demo.service/say/hi?word=" + word, String.class).getBody();
                        result =  "restTemplate: " + result;
                        observer.onNext(result);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }
    /**
     * 断路器指向的方法
     * @param word
     * @return
     */
    private String sayHiFallBack(String word){
        return "fallback" + word;
    }

}
