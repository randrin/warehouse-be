package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.services.impl.WarehouseUserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class WarehouseUserDetailsService implements UserDetailsService {

    WarehouseUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WarehouseUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + username));

        return WarehouseUserDetailsImpl.build(user);
    }
}
