package com.warehouse.bear.management.configuration;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.filter.WarehouseFilter;
import com.warehouse.bear.management.services.WarehouseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WarehouseSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private WarehouseUserDetailsService warehouseUserDetailsService;

    @Autowired
    private WarehouseFilter warehouseFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(warehouseUserDetailsService);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT,
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER,
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER)
                .permitAll()
                .antMatchers(
                        "/actuator/**",
                        "/**/entreprises/create",
                        "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**")
                .permitAll()
                .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DASHBOARD)
                .hasAnyAuthority(WarehouseUserConstants.WAREHOUSE_ROLE_ADMIN)
                .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_HOME)
                .hasAnyAuthority(WarehouseUserConstants.WAREHOUSE_ROLE_ADMIN, WarehouseUserConstants.WAREHOUSE_ROLE_MODERATOR, WarehouseUserConstants.WAREHOUSE_ROLE_USER)
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(warehouseFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
