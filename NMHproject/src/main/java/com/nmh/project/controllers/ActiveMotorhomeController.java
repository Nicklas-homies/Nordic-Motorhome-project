package com.nmh.project.controllers;

import com.nmh.project.models.RentAgreementDataHolder;
import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.DamageRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ActiveMotorhomeController {

    private MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
    private ActiveMotorhomeRepository activeMotorhomeRepository = new ActiveMotorhomeRepository();
    private DamageRepository damageRepository = new DamageRepository();

    @RequestMapping("/activeMotorhome/available")
    public String activeMotorhomes(Model model){

        ArrayList<RentAgreementDataHolder> allActiveMotorhome = activeMotorhomeRepository.getActiveMotorhome();
        model.addAttribute("motorhomes", allActiveMotorhome);

        return "activeMotorhome/available";
    }

    @RequestMapping(value = "/activeMotorhome/cancel",method = RequestMethod.POST)
    public String cancelRent(@RequestParam int rentId,/* @RequestParam int motorhomeId,*/Model model){
        ArrayList<Double> cancelPrices = activeMotorhomeRepository.getCancelPrice(rentId);

        model.addAttribute("cancelPrices", cancelPrices);
        model.addAttribute("rentId", rentId);
        return "/activeMotorhome/cancel";
    }

    @RequestMapping(value = "/activeMotorhome/cancel/yes",method = RequestMethod.POST)
    public String cancelYesRent(@RequestParam int rentId){
        activeMotorhomeRepository.cancelRentAgreement(rentId);

        return "activeMotorhome/available";
    }

    @PostMapping(value = "/activeMotorhome/return/{id}")
    public String returnMotorhomePrompt(@PathVariable("id") int rentId, @RequestParam int motorhomeId, Model model){
        // give data for return, date, dmg and so on.

        model.addAttribute("motorhome", motorhomeRepository.read(motorhomeId));
        model.addAttribute("rentId",rentId);
        return "/activeMotorhome/return";
    }

    @PostMapping("/activeMotorhome/return/price/{id}")
    public String getPriceOfReturn(@PathVariable("id") int rentId, @RequestParam int kmDriven,
                                   @RequestParam HashMap<String,String> allParams, Model model){
        int motorhomeId = Integer.parseInt(allParams.get("motorhomeId"));

        if(!allParams.get("damage").equals("")){
            String theDamage = allParams.get("damage");
            System.out.println(theDamage);
            System.out.println(motorhomeId);
            damageRepository.addDamage(theDamage,motorhomeId);
        }


        ArrayList<Double> finalTotalPrice = activeMotorhomeRepository.getFinalPrice(rentId,allParams); //first price is initial price, last is totalprice
        System.out.println(allParams);
        model.addAttribute("finalTotalPrice",finalTotalPrice);
        model.addAttribute("rentId", rentId);
        model.addAttribute("kmDriven", kmDriven);

        return "activeMotorhome/returnPrice";
    }

    @PostMapping("/activeMotorhome/return/yes")
    public String returnMotorhomeById(@RequestParam("rentId") int rentId, @RequestParam int kmDriven) {
        //all checked, home returned and all that.
        activeMotorhomeRepository.homeReturned(rentId, kmDriven);
        System.out.println(kmDriven);
        return "redirect:/activeMotorhome/available";
    }
}
