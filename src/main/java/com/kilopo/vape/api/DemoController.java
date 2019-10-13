package com.kilopo.vape.api;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/index")
    public String welcome(Map<String, Object> model) {
        return "welcome";
    }

    @RequestMapping("/private")
    public String privateOne(Map<String, Object> model) {
        return "private";
    }
}