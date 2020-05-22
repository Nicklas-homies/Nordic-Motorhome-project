package com.nmh.project.controllers;

import com.nmh.project.models.Motorhome;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MotorhomeController {
    MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
    ActiveMotorhomeRepository activeMotorhomeRepository = new ActiveMotorhomeRepository();

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/createMotorhome")
    // slet ikke motorhome her selvom det ligner den ikke bruges
    public String createMotorhome(Motorhome motorhome){
        return "createMotorhome";
    }
    @PostMapping("/createMotorhome/add")
    public String createMotorhomeAdd(@Valid Motorhome motorhome, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "/createMotorhome";
        }
        if (!motorhomeRepository.create(motorhome)) {
            System.out.println("failed to create a motorhome");
        } else {
            System.out.println("made a brand new home!!!");
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/rentMotorhome/rent",method = RequestMethod.GET)
    public String rentPage(){
        return "rentMotorhome/rent";
    }

    @PostMapping("/rentMotorhome/available")
    public String byDateRent(Model model,@RequestParam int typeId,@RequestParam String maxPrice,@RequestParam String minPrice,@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam String bikeRack, @RequestParam String bedLinen, @RequestParam String childSeat, @RequestParam String picnicTable, @RequestParam String chairs){
        //tager maxPrice og minPrice som String, så man kan tage et tomt nummer. (ingen maks pris).

        //overveje at finde en smart måde at tage alle variablerne.evt. bruge @RequestParam Map<String,String> allRequestParams
        // problemer med allrequestparams er formateringen på dates.

        //Changes data, so can tell difference between empty and filled in data.
        int tempTypeId = -1;
        int tempMinPrice = -1;
        int tempMaxPrice = -1;
        double price = -1;
        Date tempStartDate = null;
        Date tempEndDate = null;
        boolean tempBike = false;
        boolean tempBed = false;
        boolean tempChild = false;
        boolean tempPicnic = false;
        boolean tempChairs = false;
        double extraPrice = 0;
        if (typeId != 0){
            tempTypeId = typeId;
        }if (!maxPrice.equals("")){
            tempMaxPrice = Integer.parseInt(maxPrice);
        }if (!minPrice.equals("")){
            tempMinPrice = Integer.parseInt(minPrice);
        }if (!startDate.equals("")){
            try {
                tempStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            }
            catch (Exception e){
                //nothing
            }
        }if (!endDate.equals("")){
            try {
                tempEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            }
            catch (Exception e){
                //nothing
            }
        }if (bikeRack.equals("true")){
            tempBike = true;
            extraPrice += 25.0;
        }if (bedLinen.equals("true")){
            tempBed = true;
            extraPrice += 15.0;
        }if (childSeat.equals("true")){
            tempChild = true;
            extraPrice += 20.0;
        }if (picnicTable.equals("true")){
            tempPicnic = true;
            extraPrice += 20.0;
        }if (chairs.equals("true")){
            tempChairs = true;
            extraPrice += 20.0;
        }

        System.out.println(tempTypeId + ", " + tempMaxPrice + ", " +  tempMinPrice + ", " + tempStartDate + ", " + tempEndDate);
        System.out.println(extraPrice + " " + tempBike);
        model.addAttribute("motorhomes", activeMotorhomeRepository.filter(0,extraPrice,tempTypeId, tempMaxPrice, tempMinPrice, tempStartDate, tempEndDate));

        return "rentMotorhome/available";
    }

    @RequestMapping(value = "/rentMotorhome/rent/{id}", method = RequestMethod.GET)
    public String rentMotorhomeById(@PathVariable int id) {
        Motorhome homeToRent = motorhomeRepository.read(id);
        homeToRent.setActiveState(1);
        motorhomeRepository.update(homeToRent);
        return "redirect:/rentMotorhome/rent";
    }

    @RequestMapping(value = "/pickUpMotorhome/filter",method = RequestMethod.GET)
    public String pickUpFilter(){
        return "pickUpMotorhome/filter";
    }

    @PostMapping("/pickUpMotorhome/available")
    public String pickUpByDateRent(Model model, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        int tempTypeId = -1;
        int tempMinPrice = -1;
        int tempMaxPrice = -1;
        Date tempStartDate = null;
        Date tempEndDate = null;
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

        System.out.println(tempTypeId + ", " + tempMaxPrice + ", " +  tempMinPrice + ", " + tempStartDate + ", " + tempEndDate);
        model.addAttribute("motorhomes", activeMotorhomeRepository.filter(1,tempTypeId, tempMaxPrice, tempMinPrice, tempStartDate, tempEndDate));

        return "pickUpMotorhome/available";
    }

    @RequestMapping(value = "/pickUpMotorhome/filter/{id}", method = RequestMethod.GET)
    public String pickUpMotorhomeById(@PathVariable int id) {
        Motorhome homeToRent = motorhomeRepository.read(id);
        homeToRent.setActiveState(2);
        motorhomeRepository.update(homeToRent);
        return "redirect:/pickUpMotorhome/filter";
    }

    @RequestMapping(value = "/pickUpMotorhome/cancelPickUp/{id}", method = RequestMethod.GET)
    public String cancelPickUpById(@PathVariable int id) {
        Motorhome homeToPickUp = motorhomeRepository.read(id);
        homeToPickUp.setActiveState(0);
        motorhomeRepository.update(homeToPickUp);
        return "redirect:/pickUpMotorhome/filter";
    }

    @RequestMapping("/activeMotorhome/available")
    public String activeMotorhomes(Model model){
        int tempTypeId = -1;
        int tempMinPrice = -1;
        int tempMaxPrice = -1;
        Date tempStartDate = null;
        Date tempEndDate = null;

        System.out.println(tempTypeId + ", " + tempMaxPrice + ", " +  tempMinPrice + ", " + tempStartDate + ", " + tempEndDate);
        model.addAttribute("motorhomes", activeMotorhomeRepository.filter(2,tempTypeId, tempMaxPrice, tempMinPrice, tempStartDate, tempEndDate));

        return "activeMotorhome/available";
    }

    @RequestMapping(value = "/activeMotorhome/return/{id}", method = RequestMethod.GET)
    public String returnMotorhomePrompt(@PathVariable int id){
        return "/activeMotorhome/return";
    }

    @PostMapping("/activeMotorhome/return/yes/{id}")
    public String returnMotorhomeById(@PathVariable int id) {
        Motorhome homeToReturn = motorhomeRepository.read(id);
        homeToReturn.setActiveState(0);
        homeToReturn.setTimesUsed(homeToReturn.getTimesUsed() + 1);
        motorhomeRepository.update(homeToReturn);
        return "redirect:/activeMotorhome/available";
    }
}
