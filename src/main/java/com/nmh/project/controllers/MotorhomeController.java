package com.nmh.project.controllers;

import com.nmh.project.models.Customer;
import com.nmh.project.models.Motorhome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.nmh.project.models.RentAgreementDataHolder;
import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.CustomerRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import com.nmh.project.repositories.SeasonRepository;
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
    SeasonRepository seasonRepository = new SeasonRepository();
    private Date startDate;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("motorhomes/createMotorhome")
    // slet ikke motorhome her selvom det ligner den ikke bruges
    public String createMotorhome(Motorhome motorhome){
        return "motorhomes/createMotorhome";
    }

    @PostMapping("motorhomes/createMotorhome/add")
    public String createMotorhomeAdd(@Valid Motorhome motorhome, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "redirect:/motorhomes/createMotorhome";
        }

        if (!motorhomeRepository.create(motorhome)) {
            System.out.println("failed to create a motorhome");
            return "redirect:/";
        } else {
            System.out.println("made a brand new home!!!");
            return "redirect:/motorhomes/list";
        }
    }

    @RequestMapping(value = "/motorhomes/detail/{id}", method = RequestMethod.GET)
    public String motorhomeDetails(@PathVariable int id, Model model){
        model.addAttribute("motorhome", motorhomeRepository.read(id));
        model.addAttribute("damages", activeMotorhomeRepository.getDmgByMotorhomeId(id));
        return "motorhomes/detail";
    }

    @RequestMapping(value = "/motorhomes/addDmg/{id}", method = RequestMethod.GET)
    public String addDmgToMotorhomeById(@PathVariable int id, Model model){
        model.addAttribute("motorhome", motorhomeRepository.read(id));
        return "motorhomes/addDmg";
    }

    @PostMapping(value = "/motorhomes/addDmg/yes/{id}")
    public String addDmgYes(@PathVariable int id, String damage){
        String urlToReturn;

        if (activeMotorhomeRepository.addDamage(damage,id)){
            urlToReturn = "redirect:/motorhomes/edit/" + id;
            System.out.println(urlToReturn);
            return urlToReturn;
        }

        urlToReturn = "redirect:/motorhomes/detail/" + id;
        return urlToReturn;
    }

    @PostMapping("/motorhomes/deleteDmg")
    public String deleteDmgById(int dmgId, int motorhomeId){
        boolean found = false;

        if (!activeMotorhomeRepository.getAllDmg().containsKey(dmgId)){
            found = true;
        }

        if (!found){
            return "redirect:/motorhomes/list";
        }

        String urlToReturn = "redirect:/motorhomes/edit/" + motorhomeId;
        activeMotorhomeRepository.delDamage(dmgId);
        return urlToReturn;

    }

    @RequestMapping(value = "/motorhomes/delete/{id}", method = RequestMethod.GET)
    public String getMotorhomeByDeleteParam(@PathVariable int id, Model model) {
        Motorhome motorhome = motorhomeRepository.read(id);
        model.addAttribute("motorhome", motorhome);
        return "/motorhomes/delete";
    }

    @GetMapping("/motorhomes/delete/yes/{id}")
    public String delMotorhome(@PathVariable int id){
        activeMotorhomeRepository.homeReturnedByMotorhomeId(id);
        activeMotorhomeRepository.delDamageByMotorhomeId(id);
        motorhomeRepository.delete(id);
        return "redirect:/motorhomes/list";
    }

    @RequestMapping(value = "/motorhomes/edit/{id}", method = RequestMethod.GET)
    public String editMotorhomeById(@PathVariable int id, Model model){
        model.addAttribute("motorhome", motorhomeRepository.read(id));
        model.addAttribute("damages", activeMotorhomeRepository.getDmgByMotorhomeId(id));
        return "motorhomes/edit";
    }

    @PostMapping("/motorhomes/edited")
    public String editMotorhome(Motorhome motorhome){
        Motorhome motorhomeToEdit = new Motorhome();
        boolean found = false;

        for (Motorhome motorhome1 : motorhomeRepository.readAll()){
            if (motorhome1.getId() == motorhome.getId()){
                motorhomeToEdit = motorhome1;
                found = true;
                break;
            }
        }
        int id =motorhome.getId();
        String urlToReturn = "redirect:/motorhomes/detail/" + id;
        System.out.println(urlToReturn);
        if (!found){
            return "redirect:/motorhomes/list";
        }
        if (!motorhome.getBrand().equals("")){
            motorhomeToEdit.setBrand(motorhome.getBrand());
        }if (!motorhome.getModel().equals("")){
            motorhomeToEdit.setModel(motorhome.getModel());
        }if (motorhome.getTimesUsed() != -1){
            motorhomeToEdit.setTimesUsed(motorhome.getTimesUsed());
        }if (motorhome.getKmDriven() != -1){
            motorhomeToEdit.setKmDriven(motorhome.getKmDriven());
        }if (motorhome.getTypeId() != 0){
            motorhome.setTypeId(motorhome.getTypeId());
        }
        motorhomeRepository.update(motorhomeToEdit);
        return urlToReturn;
    }

    @GetMapping("motorhomes/list")
    public String motorhomesList(Model model){
        model.addAttribute("motorhomes", motorhomeRepository.readAll());
        return "motorhomes/list";
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
            double totalPrice = motorhomeRepository.getInitialProce(motorhome, allParam, startDate, endDate);

            totalPrice *= seasonRepository.seasonPrice(seasonRepository.seasonType(startDate.getMonth() + 1, endDate.getMonth() + 1));

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
                                   @RequestParam double price, @RequestParam int customerId,
                                   @RequestParam HashMap<String,String> allParams){

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

    @RequestMapping("/activeMotorhome/available")
    public String activeMotorhomes(Model model){

        ArrayList<RentAgreementDataHolder> allActiveMotorhome = activeMotorhomeRepository.getActiveMotorhome();
        model.addAttribute("motorhomes", allActiveMotorhome);

        return "activeMotorhome/available";
    }

    @RequestMapping(value = "/activeMotorhome/cancel",method = RequestMethod.POST)
    public String cancelRent(@RequestParam int rentId, @RequestParam int motorhomeId,Model model){
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

//    @RequestMapping(value = "activeMotorhome/edit/{id}", method = RequestMethod.GET)
//    public String editActiveMotorhomeById(@PathVariable("id") int rentId, Model model){
//        model.addAttribute("motorhome", activeMotorhomeRepository.getActiveMotorhomeByRentID(rentId));
//        return "activeMotorhome/edit";
//    }
//
//    @PostMapping("/activeMotorhome/edited")
//    public String editActiveMotorhome(@RequestParam int id,
//                                      @RequestParam @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss z yyyy") String startDate,
//                                      @RequestParam @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss z yyyy") String endDate){
//        try {
//            Date tempStartDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(startDate);
//            Date tempEndDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(endDate);
//        }catch (ParseException e){
//            System.out.println("Parse error at motorhomeController : editActiveMotorhome()");
//            e.printStackTrace();
//        }
//
//        String urlToReturn = "redirect:/activeMotorhome/available/";
//        System.out.println(urlToReturn);
//
//        if (activeMotorhomeRepository.updateRentPeriod(rentAgreementToEdit)){
//            return urlToReturn;
//        } else{
//            urlToReturn += data.getRentId();
//            System.out.println(urlToReturn);
//            return urlToReturn;
//        }
//    }

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
            activeMotorhomeRepository.addDamage(theDamage,motorhomeId);
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
