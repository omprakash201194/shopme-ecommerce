package com.omprakashgautam.shopme.web.backend.service;

import com.omprakashgautam.shopme.commons.entity.Role;
import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.exception.UserNotFoundException;
import com.omprakashgautam.shopme.web.backend.repository.RoleRepository;
import com.omprakashgautam.shopme.web.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author omprakash gautam
 * Created on 11-Jul-21 at 7:41 PM.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAll(){
        return userRepository.findAll();
    }

    public List<Role> listRole(){
        return roleRepository.findAll();
    }

    public void save(User user) {
        // To update user
        boolean isUpdatingUser = user.getId() != null;
        if (isUpdatingUser) {
          User existingUser = userRepository.findById(user.getId()).get();
          if (user.getPassword().isEmpty()) {
              user.setPassword(existingUser.getPassword());
          } else {
              encodePassword(user);
          }
        } else {
            encodePassword(user);
        }

        userRepository.save(user);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Long id, String email){

        Optional<User> userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail.isEmpty()) return true;

        boolean isCreatingNewUser = (null == id);
        if (isCreatingNewUser) {
            return false;
        } else return userByEmail.get().getId().equals(id);
    }

    public User getUser(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Could not find any user with id " + id));
    }
}
