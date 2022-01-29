package com.warehouse.bear.management.utils;

import com.warehouse.bear.management.model.WarehouseUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class WarehouseUserDetailsUtil implements UserDetails {

    private WarehouseUser warehouseUser;

    public WarehouseUserDetailsUtil(WarehouseUser warehouseUser) {
        super();
        this.warehouseUser = warehouseUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(warehouseUser.getRole()));
    }

    @Override
    public String getPassword() {
        return warehouseUser.getPassword();
    }

    @Override
    public String getUsername() {
        return warehouseUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
