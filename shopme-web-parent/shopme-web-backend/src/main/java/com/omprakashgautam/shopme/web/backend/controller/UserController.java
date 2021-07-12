package com.omprakashgautam.shopme.web.backend.controller;

import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author omprakash gautam
 * Created on 11-Jul-21 at 7:42 PM.
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model){
        List<User> users = userService.listAll();
        model.addAttribute("listUsers", users);
        return "users";
    }
}
