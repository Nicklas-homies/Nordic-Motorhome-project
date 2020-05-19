package com.nmh.project.controllers;

import com.nmh.project.models.Motorhome;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MotorhomeController {
    MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
    ActiveMotorhomeRepository activeMotorhomeRepository = new ActiveMotorhomeRepository();

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/rentMotorhome",method = RequestMethod.GET)
    public String rentMotorhomePage(){
        return "rentMotorhome/available";
    }

    @RequestMapping(value = "/rentMotorhome/rent",method = RequestMethod.GET)
    public String rentPage(){
        return "rentMotorhome/rent";
    }

    @PostMapping("/rentMotorhome/available")
    public String byDateRent(Model model, @RequestParam String maxPrice, @RequestParam String minPrice, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        //tager maxPrice og minPrice som String, så man kan tage et tomt nummer. (ingen maks pris).

        //overveje at finde en smart måde at tage alle variablerne.evt. bruge @RequestParam Map<String,String> allRequestParams
        // problemer med allrequestparams er formateringen på dates.

        //Changes data, so can tell difference between empty and filled in data.
        int tempMinPrice = -1;
        int tempMaxPrice = -1;
        Date tempStartDate = null;
        Date tempEndDate = null;
        if (!maxPrice.equals("")){
            tempMaxPrice = Integer.parseInt(maxPrice);
        }
        if (!minPrice.equals("")){
            tempMinPrice = Integer.parseInt(minPrice);
        }
        if (!startDate.equals("")){
            try {
                tempStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            }
            catch (Exception e){
                //nothing
            }
        }
        if (!endDate.equals("")){
            try {
                tempEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            }
            catch (Exception e){
                //nothing
            }
        }

        System.out.println(tempMaxPrice + ", " +  tempMinPrice + ", " + tempStartDate + ", " + tempEndDate);
        model.addAttribute("motorhomes", activeMotorhomeRepository.filter(tempMaxPrice, tempMinPrice, tempStartDate, tempEndDate));

        return "rentMotorhome/available";
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

        activeMotorhomeRepository.addDamage("Hjulet har røget hash", 4);
        System.out.println(activeMotorhomeRepository.getAllDmg());

        motorhomeRepository.create(testHome);
        motorhomeRepository.delete(testHome.getId());
        motorhomeRepository.delete(testHome.getId());
        motorhomeRepository.create(testHome);

        System.out.println(motorhomeRepository.readAll());
        System.out.println(motorhomeRepository.read(4));
        return "redirect:/";
    }
}
