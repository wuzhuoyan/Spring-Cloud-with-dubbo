package sample.ds.cn.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Map;

/**
 * Created by wu on 17/8/2.
 */
@Controller
public class JspControllerSample {

    @Value("${application.message:Hello World}")
    private String message = "Hello World";

    @RequestMapping("/")
    String home(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", message);
        return "welcome";
    }
}
