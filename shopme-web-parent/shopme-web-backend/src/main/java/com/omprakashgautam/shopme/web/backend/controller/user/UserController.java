package com.omprakashgautam.shopme.web.backend.controller.user;

import com.omprakashgautam.shopme.commons.entity.Role;
import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.exception.user.UserNotFoundException;
import com.omprakashgautam.shopme.web.backend.reports.user.UserCSVExporter;
import com.omprakashgautam.shopme.web.backend.reports.user.UserExcelExporter;
import com.omprakashgautam.shopme.web.backend.reports.user.UserPdfExporter;
import com.omprakashgautam.shopme.web.backend.service.UserService;
import com.omprakashgautam.shopme.web.backend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.omprakashgautam.shopme.web.backend.constants.UserConstants.*;

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
        return listByPage(1, "firstName", "asc",null, model);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model){
        Page<User> users = userService.listByPage(pageNum, sortField, sortDir, keyword);
        long startCount = (long) (pageNum - 1) * UserService.USER_PAGE_SIZE + 1;
        long totalElements = users.getTotalElements();
        if (startCount > totalElements) {
            startCount = totalElements;
        }
        long endCount = startCount + UserService.USER_PAGE_SIZE - 1;
        if (endCount > totalElements) {
            endCount = totalElements;
        }
        String reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("totalItems", totalElements);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("items", users.get().count());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listUsers", users);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortOrder", reverseSortOrder);
        model.addAttribute("keyword", keyword);
        return VIEW_ALL_USERS;
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        User user = new User();
        model.addAttribute("user",user);
        fetchRoles(model);
        model.addAttribute("pageTitle","Create New User");
        return VIEW_USERS_FORM;
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
        return getUserUrl(user);
    }

    private String getUserUrl(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return REDIRECT_TO_A_USER + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        try {
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            fetchRoles(model);
            model.addAttribute("pageTitle","Edit User ("+user.getFirstName()+")");
            return VIEW_USERS_FORM;
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message",e.getMessage());
            return REDIRECT_TO_USERS;
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
        return REDIRECT_TO_USERS;
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserStatus(@PathVariable("id") Long id, @PathVariable("status") boolean status
                                   , RedirectAttributes redirectAttributes){
        userService.updateUserStatus(id, status);
        String statusMessage = status ? "enabled" : "disabled";
        redirectAttributes.addFlashAttribute("message","The user with id ["+id+"] has been "+ statusMessage +" successfully");
        return REDIRECT_TO_USERS;
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAll();
        UserCSVExporter exporter = new UserCSVExporter();
        exporter.export(users, response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAll();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(users, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> users = userService.listAll();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(users, response);
    }
}
