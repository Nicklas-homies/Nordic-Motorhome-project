package com.nmh.project.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController implements ErrorController {
    //NOTE: AUTHORS OF THIS CLASS: JONATHAN

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String strError = "We did not expect that to happen";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());


            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("message", statusCode);
                strError = "Sorry, we couldn't find what you were looking for, try using the top menu to navigate.";
                model.addAttribute("stringError", strError);
                return "/error";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("message", statusCode);
                strError = "Sorry, we encountered an internal server error. Please try again";
                model.addAttribute("stringError", strError);
                return "/error";
            }else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                model.addAttribute("message", statusCode);
                strError = "Sorry, but that is not allowed.";
                model.addAttribute("stringError", strError);
                return "/error";
            }else if (statusCode == HttpStatus.BAD_REQUEST.value()){
                model.addAttribute("message", statusCode);
                strError = "Sorry, but that was a bad request.";
                model.addAttribute("stringError", strError);
                return "/error";
            }else {
                model.addAttribute("message", statusCode);
                model.addAttribute("stringError", strError);
                return "/error";
            }
        }
        return "/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
