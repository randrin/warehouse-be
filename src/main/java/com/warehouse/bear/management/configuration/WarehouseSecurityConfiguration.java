package com.warehouse.bear.management.configuration;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.filter.WarehouseFilter;
import com.warehouse.bear.management.services.WarehouseUserDetailsService;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WarehouseSecurityConfiguration {

    @Autowired
    private WarehouseUserDetailsService warehouseUserDetailsService;

    @Autowired
    private WarehouseFilter warehouseFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                WarehouseUserEndpoints.WAREHOUSE_ROOT,
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_LOGIN_USER,
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_RESET_PASSWORD,
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_UPLOAD_FILE,
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_FILE,
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DELETE_FILE,
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_VERIFICATION_EMAIL + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_VERIFY_TOKEN + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_FORGOT_PASSWORD + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_VERIFY_USER_LINK_TYPE + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_ACTIVATE_OR_DISABLED + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_FIND_USER + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_GLOSSARIES + "/**",
                                WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_INSERT_ORGANIZATION + "/**",
                                "/actuator/**",
                                "/**/entreprises/create",
                                "/v2/api-docs",
                                "/swagger-resources/**",
                                "/configuration/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**"
                        ).permitAll()
                        .requestMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DASHBOARD)
                        .hasAuthority(WarehouseUserConstants.WAREHOUSE_ROLE_ADMIN)
                        .requestMatchers(WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_HOME)
                        .hasAnyAuthority(
                                WarehouseUserConstants.WAREHOUSE_ROLE_ADMIN,
                                WarehouseUserConstants.WAREHOUSE_ROLE_MODERATOR,
                                WarehouseUserConstants.WAREHOUSE_ROLE_USER
                        )
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(warehouseFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(warehouseUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

        return authBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WarehouseCommonUtil warehouseCommonUtil() {
        return new WarehouseCommonUtil();
    }
}

