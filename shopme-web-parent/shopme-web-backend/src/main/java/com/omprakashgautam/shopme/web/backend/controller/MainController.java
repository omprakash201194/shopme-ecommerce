package com.omprakashgautam.shopme.web.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author omprakash gautam
 * Created on 10-Jul-21 at 10:54 PM.
 */
@Controller
public class MainController {

    @GetMapping("")
    public String viewHomePage(){
        return "index";
    }
}
