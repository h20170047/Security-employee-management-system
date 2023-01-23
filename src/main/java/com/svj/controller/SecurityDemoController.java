package com.svj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityDemoController {
    @GetMapping("/home")
    public String welcome(){
        return "Welcome! ";
    }

    @GetMapping("/nonSecure")
    public String nonSecure(){
        return "This is not a secured endpoint";
    }
}
