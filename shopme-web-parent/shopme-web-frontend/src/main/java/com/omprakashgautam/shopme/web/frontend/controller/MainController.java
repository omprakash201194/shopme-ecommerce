package com.omprakashgautam.shopme.web.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author omprakash gautam
 * Created on 10-Jul-21 at 11:08 PM.
 */
@Controller
public class MainController {
    @GetMapping("")
    public String viewHomePage(){
        return "index";
    }
}
