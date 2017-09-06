package sample.ds.cn.consumer;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.gac.activity.model.ActivityApplyModel;
import com.gac.activity.service.ActivityService;
import com.gac.core.resp.Response;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.ds.cn.common.ContextUtil;

/**
 * Created by wu on 17/8/18.
 */

@Service
public class DubboConsumerSample {

    /* Generic mode, no dependices */
    public Object callDubboService(String dubboServiceName, String methodName, String[] paramTypes, Object[] paramValues) {
        GenericService genericService = (GenericService) ContextUtil.getBean(dubboServiceName);
        Object result = genericService.$invoke(methodName, paramTypes, paramValues);
        return result;
    }


    /* Interface mode, dependices */
    @Autowired
    private ActivityService activityService;

    @HystrixCommand(
            fallbackMethod = "fallBackFindActivityApplyDetailed",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000"),
            }
    )
    public Object findActivityApplyDetailed(String activityApplyId) throws Exception {
        if(Math.random()>0.2){
            throw new Exception();
        }else{
            Response<ActivityApplyModel> response = activityService.findActivityApplyDetailed(activityApplyId);
            if (response.isSuccess() && response.getData() != null) {
                return response.getData();
            }else{
                throw new Exception();
            }
        }
    }


    /* fall back */
    private Object fallBackFindActivityApplyDetailed(String activityApplyId){
        return "dubbo fall back";
    }
}
