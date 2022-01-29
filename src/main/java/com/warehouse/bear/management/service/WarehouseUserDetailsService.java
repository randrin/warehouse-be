package com.warehouse.bear.management.service;

import com.warehouse.bear.management.exception.UserNotFoundException;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.utils.WarehouseUserDetailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WarehouseUserDetailsService implements UserDetailsService {

    @Autowired
    private WarehouseUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WarehouseUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username + " Not Found");
        }
        return new WarehouseUserDetailsUtil(user);
    }
}
