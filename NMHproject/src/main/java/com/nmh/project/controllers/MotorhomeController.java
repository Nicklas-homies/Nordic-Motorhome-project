package com.nmh.project.controllers;

import com.nmh.project.models.Motorhome;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MotorhomeController {
    MotorhomeRepository motorhomeRepository = new MotorhomeRepository();

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/rentMotorhome",method = RequestMethod.GET)
    public String rentPage(){
        return "rent";
    }

    @PostMapping("/rentMotorhome/date")
    public String byDateRent(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        System.out.println(startDate);
        System.out.println(endDate);
//now check whats avaible
        System.out.println(motorhomeRepository.avaibleByDate(startDate,endDate)); //gets an arraylist with motorhomes. ez to put into table or some such.

        return "redirect:/rentMotorhome";
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

        motorhomeRepository.addDamage("Hjulet har røget hash", 4);
        System.out.println(motorhomeRepository.getAllDmg());

        motorhomeRepository.create(testHome);
        motorhomeRepository.delete(testHome.getId());
        motorhomeRepository.delete(testHome.getId());
        motorhomeRepository.create(testHome);

        System.out.println(motorhomeRepository.readAll());
        System.out.println(motorhomeRepository.read(4));
        return "redirect:/";
    }
}
