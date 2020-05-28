package com.nmh.project.controllers;

import com.nmh.project.models.Motorhome;

import com.nmh.project.repositories.ActiveMotorhomeRepository;
import com.nmh.project.repositories.DamageRepository;
import com.nmh.project.repositories.MotorhomeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MotorhomeController {
    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private MotorhomeRepository motorhomeRepository = new MotorhomeRepository();
    private ActiveMotorhomeRepository activeMotorhomeRepository = new ActiveMotorhomeRepository();
    private DamageRepository damageRepository = new DamageRepository();

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
        model.addAttribute("damages", damageRepository.getDmgByMotorhomeId(id));
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

        if (damageRepository.addDamage(damage,id)){
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

        if (!damageRepository.getAllDmg().containsKey(dmgId)){
            found = true;
        }

        if (!found){
            return "redirect:/motorhomes/list";
        }

        String urlToReturn = "redirect:/motorhomes/edit/" + motorhomeId;
        damageRepository.delDamage(dmgId);
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
        damageRepository.delDamageByMotorhomeId(id);
        motorhomeRepository.delete(id);
        return "redirect:/motorhomes/list";
    }

    @RequestMapping(value = "/motorhomes/edit/{id}", method = RequestMethod.GET)
    public String editMotorhomeById(@PathVariable int id, Model model){
        model.addAttribute("motorhome", motorhomeRepository.read(id));
        model.addAttribute("damages", damageRepository.getDmgByMotorhomeId(id));
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
}
