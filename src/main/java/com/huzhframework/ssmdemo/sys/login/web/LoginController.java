package com.huzhframework.ssmdemo.sys.login.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        return "sys/index/login";
    }

    @RequestMapping("/index")
    public String index() {
        return "sys/index/index";
    }
}
