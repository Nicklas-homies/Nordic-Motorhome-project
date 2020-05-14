package com.nmh.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class MotorhomeController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
