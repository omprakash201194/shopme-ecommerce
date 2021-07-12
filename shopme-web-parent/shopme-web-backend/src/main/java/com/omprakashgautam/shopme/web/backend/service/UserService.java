package com.omprakashgautam.shopme.web.backend.service;

import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author omprakash gautam
 * Created on 11-Jul-21 at 7:41 PM.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> listAll(){
        return userRepository.findAll();
    }
}
