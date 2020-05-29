package com.nmh.project.controllers;

import com.nmh.project.models.Customer;
import com.nmh.project.models.Motorhome;
import com.nmh.project.repositories.CustomerRepository;
import com.nmh.project.repositories.FilterRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Controller
public class RentMotorhomeController {
    private MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
    private CustomerRepository customerRepository = new CustomerRepository();
    private FilterRepository filterRepository = new FilterRepository();

    @RequestMapping(value = "/rentMotorhome/rent",method = RequestMethod.GET)
    public String rentPage(){
        return "rentMotorhome/rent";
    }

    @PostMapping("/rentMotorhome/available")
    public String byDateRent(Model model, @RequestParam int typeId, @RequestParam String maxPrice, @RequestParam String minPrice,
                             @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        //tager maxPrice og minPrice som String, så man kan tage et tomt nummer. (ingen maks pris).

        //overveje at finde en smart måde at tage alle variablerne.evt. bruge @RequestParam Map<String,String> allRequestParams
        // problemer med allrequestparams er formateringen på dates.

        //Changes data, so can tell difference between empty and filled in data.
        int tempTypeId = -1;
        int tempMinPrice = -1;
        int tempMaxPrice = -1;
        Date tempStartDate = null;
        Date tempEndDate = null;

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
            catch (ParseException e){
                e.printStackTrace();
            }
        }
        if (!endDate.equals("")){
            try {
                tempEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            }
            catch (ParseException e){
                e.printStackTrace();
            }
        }


        System.out.println(tempTypeId + ", " + tempMaxPrice + ", " +  tempMinPrice + ", " + tempStartDate + ", " + tempEndDate);
        model.addAttribute("motorhomes", filterRepository.filter(0,tempTypeId, tempMaxPrice, tempMinPrice, tempStartDate, tempEndDate));
        model.addAttribute("startDate", tempStartDate);
        model.addAttribute("endDate", tempEndDate);
        return "rentMotorhome/available";
    }

    //EEE MMM dd HH:mm:ss z yyyy
    @PostMapping("rentMotorhome/confirm")
    public String confirmRent(@RequestParam int motorhomeId, @RequestParam String endDate, @RequestParam String startDate, Model model){

        model.addAttribute("motorhome", motorhomeRepository.read(motorhomeId));
        model.addAttribute("customers", customerRepository.readAll());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "rentMotorhome/confirmRent";
    }

    @PostMapping("/rentMotorhome/confirmed")
    public String confirmed(@RequestParam HashMap<String, String> allParam,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                            @RequestParam int customerId,Model model){
        int id = Integer.parseInt(allParam.get("motorhomeId"));
        boolean found = false;

        for (Customer customer : customerRepository.readAll()){
            if (customer.getId() == customerId){
                Customer customer1 = customerRepository.read(customerId);
                model.addAttribute("customer", customer1);
                found = true;
                break;
            }
        }
        if (found) {

            Motorhome motorhome = motorhomeRepository.read(id);
            double totalPrice = motorhomeRepository.getInitialPrice(motorhome, allParam, startDate, endDate);

            model.addAttribute("motorhome", motorhome);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("price", totalPrice);

            return "rentMotorhome/confirmRentPrice";
        }else{
            return "redirect:/";
        }
    }

    @PostMapping("/rentMotorhome/newRentConfirmed")
    public String everythingIsGood(@RequestParam @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss z yyyy") String startDate,
                                   @RequestParam @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss z yyyy") String endDate, @RequestParam int motorhomeId,
                                   @RequestParam double price, @RequestParam int customerId/*,
                                   @RequestParam HashMap<String,String> allParams*/){

        try {
            Date tempStartDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(startDate);
            Date tempEndDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(endDate);

            motorhomeRepository.newRentDeal(tempStartDate, tempEndDate, price, customerId, motorhomeId);
        }catch (ParseException e){
            System.out.println("Error at motorhomeController : everythingIsGood()");
            e.printStackTrace();
        }

        return "rentMotorhome/rent";
    }

    @RequestMapping(value = "/rentMotorhome/rent/{id}", method = RequestMethod.GET)
    public String rentMotorhomeById(@PathVariable int id) {
        Motorhome homeToRent = motorhomeRepository.read(id);
        homeToRent.setActiveState(1);
        motorhomeRepository.update(homeToRent);
        return "redirect:/rentMotorhome/rent";
    }
}
