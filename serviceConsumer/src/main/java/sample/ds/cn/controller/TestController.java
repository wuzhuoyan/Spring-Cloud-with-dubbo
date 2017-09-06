package sample.ds.cn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wu on 17/8/3.
 */
@RestController

@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
public class TestController {

    /*Spring DiscoveryClient*/
    @Autowired
    org.springframework.cloud.client.discovery.DiscoveryClient springDiscoveryClient;

    @RequestMapping(value = "/listServices")
    public String show(){
        String re = "";
        List<String> services = springDiscoveryClient.getServices();
        for(String per : services){
            re = re + per;

            List<ServiceInstance> instances = springDiscoveryClient.getInstances(per);
            String x = "";
        }

        return re;
    }

    @Autowired
    RestTemplate restTemplate;

    private static HashMap<String, List<ServiceInstance>> servicesCache  = new HashMap<String, List<ServiceInstance>>();

    @RequestMapping(value = "initClients")
    public String test(){
        List<String> services = springDiscoveryClient.getServices();
        for(String perServiceName : services){
            if(perServiceName.equals("eureka.node")){
                continue;
            }
            if(!servicesCache.keySet().contains(perServiceName)){
                servicesCache.put(perServiceName, new ArrayList<ServiceInstance>());
            }
            List<ServiceInstance> instances = springDiscoveryClient.getInstances(perServiceName);
            for(ServiceInstance instance : instances){
                servicesCache.get(perServiceName).add(instance);
            }
        }
        return "end";
    }

    private static final String SERVICE = "service0";
    private static final String METHOD = "method0";
    private static final String SIGN = "sign0";

    @RequestMapping(value = "callService", method = RequestMethod.GET)
    public String callService(HttpServletRequest request){

        Map<String, String[]> parameterMap = request.getParameterMap();
        String service = "";
        String method = "";
        String sign = "";
        try{
            service = parameterMap.get(SERVICE)[0].trim();
            method = parameterMap.get(METHOD)[0].trim();
            sign = parameterMap.get(SIGN)[0].trim();
        } catch (Exception e){
            //TODO
        }

        if(StringUtils.isEmpty(service)){
            return "传入服务标识不能为空";
        }
        if(StringUtils.isEmpty(method)){
            return "传入方法标识不能为空";
        }
//        if(StringUtils.isEmpty(sign)){
//            return "传入签名标识不能为空";
//        }

        String methodPartStr = "";
        if(method.indexOf(".")>0){
            methodPartStr = method.replaceAll("\\.", "/");
        }else{
            methodPartStr = method;
        }

        String params = "";
        Iterator it = parameterMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String[]> entry = (Map.Entry) it.next();
            if(entry.getKey().equals(SERVICE) || entry.getKey().equals(METHOD) || entry.getKey().equals(SIGN)){
                continue;
            }
            for(String val : entry.getValue()){
                params = params + entry.getKey() + "=" + val + "&";
            }
        }
        if(params.endsWith("&")){
            params = params.substring(0, params.length()-1);
        }

        String callUrl = "http://"+service+"/"+methodPartStr+"?"+params;

        String result = restTemplate.getForEntity(callUrl, String.class).getBody();

        return result;


    }

}
