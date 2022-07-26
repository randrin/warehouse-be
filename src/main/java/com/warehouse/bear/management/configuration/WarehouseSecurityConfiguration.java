package com.warehouse.bear.management.configuration;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.filter.WarehouseFilter;
import com.warehouse.bear.management.model.WarehouseRole;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.repository.WarehouseRoleRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.services.WarehouseUserDetailsService;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WarehouseSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseRoleRepository roleRepository;

    @Autowired
    private WarehouseUserDetailsService warehouseUserDetailsService;

    @Autowired
    private WarehouseFilter warehouseFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(warehouseUserDetailsService);
    }

    @Value("${spring.profiles.active}")
    private String production;


    @PostConstruct
    public void initUsers() {
        System.out.println("production: " + production);
        if (production.compareToIgnoreCase("dev") == 0) {
            List<WarehouseRole> roles = Stream.of(
                    new WarehouseRole(0L, WarehouseUserConstants.WAREHOUSE_ROLE_ADMIN, "Admin role"),
                    new WarehouseRole(0L, WarehouseUserConstants.WAREHOUSE_ROLE_MODERATOR, "Moderator role"),
                    new WarehouseRole(0L, WarehouseUserConstants.WAREHOUSE_ROLE_USER, "User role")
            ).collect(Collectors.toList());
            roleRepository.saveAll(roles);

            List<WarehouseUser> users = Stream.of(
                    new WarehouseUser(0L, WarehouseCommonUtil.generateUserId(), "randrino17", "Nzeukang Randrin", "Male", "nzeukangrandrin@gmail.com", "", bCryptPasswordEncoder().encode("123456789"), Stream.of(new WarehouseRole(1L, WarehouseUserConstants.WAREHOUSE_ROLE_ADMIN, "Admin role")).collect(Collectors.toList()), "", true, WarehouseCommonUtil.generateCurrentDateUtil(), WarehouseCommonUtil.generateCurrentDateUtil()),
                    new WarehouseUser(0L, WarehouseCommonUtil.generateUserId(), "rodrigo", "Djomou Rodrigue", "Male", "djomoutresor1@hotmail.fr", "", bCryptPasswordEncoder().encode("123456789"), Stream.of(new WarehouseRole(1L, WarehouseUserConstants.WAREHOUSE_ROLE_ADMIN, "Admin role")).collect(Collectors.toList()), "", true, WarehouseCommonUtil.generateCurrentDateUtil(), WarehouseCommonUtil.generateCurrentDateUtil())
            ).collect(Collectors.toList());
            userRepository.saveAll(users);
        }
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
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_REGISTER_USER + "/**",
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_RESET_PASSWORD,
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_UPLOAD_FILE,
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_FILE,
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_DELETE_FILE,
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_VERIFICATION_EMAIL+ "/**",
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_VERIFY_TOKEN + "/**",
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_FORGOT_PASSWORD + "/**",
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_VERIFY_USER_LINK_TYPE + "/**",
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_ACTIVATE_OR_DISABLED + "/**",
                        WarehouseUserEndpoints.WAREHOUSE_ROOT_ENDPOINT + WarehouseUserEndpoints.WAREHOUSE_FIND_USER + "/**")
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

    @Bean
    public WarehouseCommonUtil warehouseCommonUtil() {
        return new WarehouseCommonUtil();
    }
}
