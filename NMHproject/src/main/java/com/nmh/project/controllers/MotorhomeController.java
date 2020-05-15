package com.nmh.project.controllers;

import com.nmh.project.models.Motorhome;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MotorhomeController {

    MotorhomeRepository motorhomeRepository = new MotorhomeRepository();

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/testingTests", method = RequestMethod.GET)
    public String testingTests(){
        Motorhome testHome = new Motorhome();
        testHome.setId(4);
        testHome.setKmDriven(343);
        testHome.setTypeId(1);
        testHome.setExtraPrice(4.2);
        testHome.setBrand("this fat bizz");
        testHome.setModel("dizz fat bizz");

        motorhomeRepository.create(testHome);
        motorhomeRepository.delete(testHome.getId());
        motorhomeRepository.delete(testHome.getId());
        motorhomeRepository.create(testHome);

        System.out.println(motorhomeRepository.readAll());
        System.out.println(motorhomeRepository.read(4));
        return "redirect:/";
    }
}
