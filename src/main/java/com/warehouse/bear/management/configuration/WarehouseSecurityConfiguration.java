package com.warehouse.bear.management.configuration;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.service.WarehouseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WarehouseSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private WarehouseUserDetailsService warehouseUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(warehouseUserDetailsService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT)
                .permitAll()
                .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DASHBOARD)
                .hasAnyAuthority(WarehouseUserConstants.USER_ROLE_ADMIN)
                .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_HOME)
                .hasAnyAuthority(WarehouseUserConstants.USER_ROLE_USER)
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
