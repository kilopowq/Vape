package com.kilopo.vape.api;

import com.kilopo.vape.configuration.external.LoginConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final LoginConfig config;

    LoginController(LoginConfig config) {
        this.config = config;
    }

    @GetMapping
    public String login(Model model) {
        return "login";
    }

    @GetMapping(path = "/config")
    public @ResponseBody
    LoginConfig action() {
        return config;
    }
}
