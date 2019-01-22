package com.huzhframework.ssmdemo.sys.login.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String login() {
        return "sys/index/index";
    }
}
