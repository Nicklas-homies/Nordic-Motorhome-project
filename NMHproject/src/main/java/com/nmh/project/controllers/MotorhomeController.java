package com.nmh.project.controllers;

import com.nmh.project.models.Customer;
import com.nmh.project.models.Motorhome;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.nmh.project.models.RentAgreementDataHolder;
import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.CustomerRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MotorhomeController {
    MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
    ActiveMotorhomeRepository activeMotorhomeRepository = new ActiveMotorhomeRepository();
    CustomerRepository customerRepository = new CustomerRepository();
    private Date startDate;

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
    public String byDateRent(Model model,@RequestParam int typeId,@RequestParam String maxPrice,@RequestParam String minPrice,
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
        model.addAttribute("motorhomes", activeMotorhomeRepository.filter(0,tempTypeId, tempMaxPrice, tempMinPrice, tempStartDate, tempEndDate));
        model.addAttribute("startDate", tempStartDate);
        model.addAttribute("endDate", tempEndDate);
        return "rentMotorhome/available";
    }

    //EEE MMM dd HH:mm:ss z yyyy
    @PostMapping("rentMotorhome/confirm")
    public String confirmRent(@RequestParam int motorhomeId, @RequestParam String endDate, @RequestParam String startDate, Model model){
        System.out.println("hello");
        System.out.println(motorhomeId);
        System.out.println(endDate);
        System.out.println(startDate); //forkert format, kan ikke få dem til at foreslå selv i html.


        model.addAttribute("motorhome", motorhomeRepository.read(motorhomeId));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "rentMotorhome/confirmRent";
    }

    @PostMapping("/rentMotorhome/confirmed")
    public String confirmed(@RequestParam HashMap<String, String> allParam, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, Model model){
        System.out.println("1 " + allParam);
        System.out.println("2 " + startDate);
        System.out.println("3 " + endDate);

        int id = Integer.parseInt(allParam.get("motorhomeId"));
        int customerNumber = Integer.parseInt(allParam.get("customerNumber"));
        Customer customer = new Customer();
        customer.setcName(allParam.get("customerName"));
        customer.setNumber(customerNumber);
        Motorhome motorhome = motorhomeRepository.read(id);
        //Set price based on date, and esktra. //call anothermethod from somewhere!
        //ask user to confirm. //update SQL


        double totalPrice = motorhomeRepository.getInitialProce(motorhome, allParam, startDate, endDate);
        System.out.println(totalPrice);

        model.addAttribute("motorhome", motorhome);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("price",totalPrice);
        model.addAttribute("customer", customer);


        return "rentMotorhome/confirmRentPrice";
    }

    @PostMapping("/rentMotorhome/newRentConfirmed")
    public String everythingIsGood(@RequestParam @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss z yyyy") String startDate,
                                   @RequestParam @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss z yyyy") String endDate, @RequestParam int motorhomeId,
                                   @RequestParam double price, @RequestParam String cName, @RequestParam int number,
                                   @RequestParam HashMap<String,String> allParams){
        System.out.println(allParams);
        Customer customer = new Customer();
        customer.setNumber(number);
        customer.setcName(cName);
        int customerId = customerRepository.create(customer);
        try {
            Date tempStartDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(startDate);
            Date tempEndDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(endDate);

            System.out.println(motorhomeRepository.newRentDeal(tempStartDate, tempEndDate, price, customerId, motorhomeId));
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

        ArrayList<RentAgreementDataHolder> allActiveMotorhome = activeMotorhomeRepository.getActiveMotorhome();
        model.addAttribute("motorhomes", allActiveMotorhome);

        return "activeMotorhome/available";
    }
//todo: update so rentId follows entire rent part, so we know at the end what custusemotor to remove, since we have to use a unique key.
//todo: finish the price method.
//todo: finish the confirm return with price (simply all returnMotorhomeById) at controller.


    @PostMapping(value = "/activeMotorhome/return/{id}")
    public String returnMotorhomePrompt(@PathVariable("id") int rentId, @RequestParam int motorhomeId, Model model){
    // give data for return, date, dmg and so on.

        model.addAttribute("motorhome", motorhomeRepository.read(motorhomeId));
        model.addAttribute("rentId",rentId);
        return "/activeMotorhome/return";
    }

    @PostMapping("/activeMotorhome/return/price/{id}")
    public String getPriceOfReturn(@PathVariable("id") int rentId, @RequestParam HashMap<String,String> allParams, Model model){
        if(!allParams.get("damage").equals("")){
            String theDamage = allParams.get("damage");
            System.out.println(theDamage);
            int motorhomeId = Integer.parseInt(allParams.get("motorhomeId"));
            System.out.println(motorhomeId);
            activeMotorhomeRepository.addDamage(theDamage,motorhomeId);


        }
        ArrayList<Double> finalTotalPrice = activeMotorhomeRepository.getFinalPrice(rentId,allParams); //first price is initial price, last is totalprice
        System.out.println(allParams);
        model.addAttribute("finalTotalPrice",finalTotalPrice);
        model.addAttribute("rentId", rentId);

        return "activeMotorhome/returnPrice";
    }

    @PostMapping("/activeMotorhome/return/yes")
    public String returnMotorhomeById(@RequestParam("rentId") int rentId) {
        //all checked, home returned and all that.
        activeMotorhomeRepository.homeReturned(rentId);
        return "redirect:/activeMotorhome/available";
    }
}
