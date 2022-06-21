package com.warehouse.bear.management.services;

import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.admin.WarehouseAdminUser;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.admin.WarehouseAdminUserRepository;
import com.warehouse.bear.management.services.impl.WarehouseUserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WarehouseUserDetailsService implements UserDetailsService {

    @Autowired
    WarehouseUserRepository userRepository;

    @Autowired
    WarehouseAdminUserRepository adminUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<WarehouseUser> user = null;
        Optional<WarehouseAdminUser> adminUser = null;

        user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            adminUser = adminUserRepository.findByUsername(username);
        }
        return user.isPresent() ? WarehouseUserDetailsImpl.build(user.get()) : WarehouseUserDetailsImpl.build(adminUser.get());
    }
}
