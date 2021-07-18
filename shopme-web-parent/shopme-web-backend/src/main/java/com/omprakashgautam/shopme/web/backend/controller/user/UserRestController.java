package com.omprakashgautam.shopme.web.backend.controller.user;

import com.omprakashgautam.shopme.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author omprakash gautam
 * Created on 13-Jul-21 at 4:41 PM.
 */
@RestController
public class UserRestController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/check-email")
    public String checkDuplicateEmail(@Param("id") Long id, @Param("email") String email){
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
