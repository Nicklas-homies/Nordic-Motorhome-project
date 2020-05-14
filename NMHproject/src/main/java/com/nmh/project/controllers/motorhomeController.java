package com.nmh.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class motorhomeController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
