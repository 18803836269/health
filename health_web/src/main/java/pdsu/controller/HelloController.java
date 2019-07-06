package pdsu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pdsu.service.HelloService;

/**
 * @RestController相当于@Controller和@ResponseBody
 */
@RestController
@RequestMapping("hello")
public class HelloController {

    @Reference
    HelloService helloService;

    @RequestMapping("sayHello")
    public String sayHello(String name){
        return helloService.sayHello(name);
    }
}
