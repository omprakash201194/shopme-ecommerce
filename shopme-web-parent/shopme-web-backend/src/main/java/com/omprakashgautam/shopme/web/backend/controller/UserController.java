package com.omprakashgautam.shopme.web.backend.controller;

import com.omprakashgautam.shopme.commons.entity.Role;
import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.exception.UserNotFoundException;
import com.omprakashgautam.shopme.web.backend.service.UserService;
import com.omprakashgautam.shopme.web.backend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    public String listFirstPage(Model model){
        return listByPage(1, model);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model){
        Page<User> users = userService.listByPage(pageNum);
        long startCount = (long) (pageNum - 1) * UserService.USER_PAGE_SIZE + 1;
        long totalElements = users.getTotalElements();
        if (startCount > totalElements) {
            startCount = totalElements;
        }
        long endCount = startCount + UserService.USER_PAGE_SIZE - 1;
        if (endCount > totalElements) {
            endCount = totalElements;
        }
        model.addAttribute("totalItems", totalElements);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("items", users.get().count());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
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
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            User savedUser = userService.save(user);
        }
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
