package com.omprakashgautam.shopme.web.backend.security.user;

import com.omprakashgautam.shopme.commons.entity.User;
import com.omprakashgautam.shopme.web.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * @author omprakash gautam
 * Created on 16-Jul-21 at 6:21 PM.
 */
public class ShopmeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.getUserByEmail(email);
        return userByEmail.map(ShopmeUserDetails::new).orElseThrow(() ->
            new UsernameNotFoundException("Could not find user with email: "+email)
        );
    }
}
