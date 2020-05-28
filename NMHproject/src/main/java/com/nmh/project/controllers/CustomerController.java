package com.nmh.project.controllers;

import com.nmh.project.models.Customer;
import com.nmh.project.repositories.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {
    //NOTE: AUTHORS OF THIS CLASS: ALLE
    private CustomerRepository customerRepository;


    public CustomerController() {
        this.customerRepository = new CustomerRepository();
    }

    @GetMapping("/customer")
    @ResponseBody
    public String getCustomerByParameter(@RequestParam int id) {
        Customer cus = customerRepository.read(id);
        return "This id belongs to " + cus.getcName() + " " + cus.getNumber();
    }

    @GetMapping("/customer/list")
    public String getCustomers(Model model){
        model.addAttribute("customers", customerRepository.readAll());
        return "/customer/list";
    }

    @RequestMapping(value = "/customer/detail/{id}", method = RequestMethod.GET)
    public String getCustomerByDetailParam(@PathVariable int id, Model model) {
        Customer cus = customerRepository.read(id);
        model.addAttribute("customer", cus);
        return "/customer/detail";
    }

    @RequestMapping(value = "/customer/delete/{id}", method = RequestMethod.GET)
    public String getCustomerByDeleteParam(@PathVariable int id, Model model) {
        Customer cus = customerRepository.read(id);
        model.addAttribute("customer", cus);
        return "/customer/delete";
    }

    @GetMapping("/customer/delete/yes/{id}")
    public String delCustomer(@PathVariable int id){
        if (customerRepository.delete(id)){
            return "redirect:/customer/list";
        }else{
            return "redirect:/customer/errorDel";
        }

    }

    @GetMapping("/customer/errorDel")
    public String delCustError(){
        return "customer/errorDel";
    }

    @RequestMapping(value = "/customer/edit/{id}", method = RequestMethod.GET)
    public String getEditById(@PathVariable int id, Model model) {
        Customer cus = customerRepository.read(id);
        model.addAttribute("customer", cus);
        return "/customer/edit";
    }

    @PostMapping("/customer/edited")
    public String editCustomer(Customer customer){
        Customer customerToEdit = new Customer();
        boolean found = false;

        for (Customer customer2 : customerRepository.readAll()){
            if (customer2.getId() == customer.getId()){
                customerToEdit = customer2;
                found = true;
                break;
            }
        }
        if (!found){
            return "redirect:/";
        }
        if (!customer.getcName().equals("")){
            customerToEdit.setcName(customer.getcName());
        }if (customer.getNumber() != 00000000){
            customerToEdit.setNumber(customer.getNumber());
        }
        customerRepository.update(customerToEdit);
        return "redirect:/customer/list";
    }

    //Create mapping
    @GetMapping("/customer/create")
    public String create(){
        return "/customer/create";
    }

    @PostMapping("/customer/created")
    public String createCustomer(Customer customer){
        customerRepository.create(customer);
        return "redirect:/customer/list";
    }

}
