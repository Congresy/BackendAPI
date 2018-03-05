package com.conferencias.tfg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SwaggerController {

    @RequestMapping("/")
    public ModelAndView welcome(){
        return new ModelAndView("redirect:/swagger-ui.html");
    }
}
