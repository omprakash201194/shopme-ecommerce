package com.omprakashgautam.shopme.web.backend.controller;

import com.omprakashgautam.shopme.commons.entity.Role;
import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.exception.UserNotFoundException;
import com.omprakashgautam.shopme.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/users/new")
    public String newUser(Model model){
        User user = new User();
        model.addAttribute("user",user);
        fetchRoles(model);
        model.addAttribute("pageTitle","Create New User");
        return "users_form";
    }

    private void fetchRoles(Model model) {
        List<Role> roles = userService.listRole();
        model.addAttribute("listRoles",roles);
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        userService.save(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        try {
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            fetchRoles(model);
            model.addAttribute("pageTitle","Edit User ("+user.getFirstName()+")");
            return "users_form";
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message","The user with id ["+id+"] has been deleted successfully");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserStatus(@PathVariable("id") Long id, @PathVariable("status") boolean status
                                   , RedirectAttributes redirectAttributes){
        userService.updateUserStatus(id, status);
        String statusMessage = status ? "enabled" : "disabled";
        redirectAttributes.addFlashAttribute("message","The user with id ["+id+"] has been "+ statusMessage +" successfully");
        return "redirect:/users";
    }
}
