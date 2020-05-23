package com.nmh.project.controllers;

import com.nmh.project.models.Customer;
import com.nmh.project.repositories.CustomerRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public class CustomerController {
    CustomerRepository customerRepository;


    public CustomerController() {
        this.customerRepository = new CustomerRepository();
    }

    @GetMapping("/Customer")
    @ResponseBody
    public String getCustomerByParameter(@RequestParam int id) {
        Customer cus = customerRepository.read(id);
        return "This id belongs to " + cus.getcName() + " " + cus.getNumber();
    }

    @RequestMapping(value = "/customer/detail/{id}", method = RequestMethod.GET)
    public String getCustomerByDetailParam(@PathVariable int id, Model model) {
        Customer cus = customerRepository.read(id);
        model.addAttribute("customer", cus);
        return "/customer/detail";
    }

    @RequestMapping(value = "/customer/delete/{id}", method = RequestMethod.GET)
    public String getStudentByDeleteParam(@PathVariable int id, Model model) {
        Customer cus = customerRepository.read(id);
        model.addAttribute("customer", cus);
        return "/customer/delete";
    }

    @GetMapping("/customer/delete/yes/{id}")
    public String delCustomer(@PathVariable int id){
        customerRepository.delete(id);
        return "redirect:/";
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
        }
        customerRepository.update(customerToEdit);
        return "redirect:/";
    }

    //Create mapping
    @GetMapping("/customer/create")
    public String create(){
        return "/customer/create";
    }

    @PostMapping("/customer/created")
    public String createCustomer(Customer customer){
        customerRepository.create(customer);
        return "redirect:/";
    }

}