package com.example.demo.domain.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthController {


    @GetMapping({"", "/"})
    public String getAuthorizationMessage() {
        return "home";
    }

    @GetMapping("/sns")
    public String login() {
        return "sns";
    }

    @GetMapping({"/loginSuccess", "/hello"})
    public String loginSuccess() {
        return "hello";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}
