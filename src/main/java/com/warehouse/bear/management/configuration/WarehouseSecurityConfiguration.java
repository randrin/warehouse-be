package com.warehouse.bear.management.configuration;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.warehouseSecurity.jwt.AuthEntryPointJwt;
import com.warehouse.bear.management.warehouseSecurity.jwt.AuthTokenFilter;
import com.warehouse.bear.management.services.WarehouseUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WarehouseSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private WarehouseUserDetailsServiceImpl warehouseUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(warehouseUserDetailsService);
    }

    @Autowired
    WarehouseUserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();}

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()
                    .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT).permitAll()
                    .antMatchers("/v1/warehouse/**").permitAll()
                    .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT)
                    .permitAll()
                    .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT).permitAll()
                    .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DASHBOARD)
                    .hasAnyAuthority(WarehouseUserConstants.USER_ROLE_ADMIN)
                    .antMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_HOME)
                    .hasAnyAuthority(WarehouseUserConstants.USER_ROLE_USER)
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();

        }

    }








