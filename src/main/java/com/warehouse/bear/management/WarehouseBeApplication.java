package com.warehouse.bear.management;

import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class WarehouseBeApplication {

//    @Autowired
//    private WarehouseUserRepository userRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Value("${spring.profiles.active}")
//    private String production;

    public static void main(String[] args) {
        SpringApplication.run(WarehouseBeApplication.class, args);
    }

//    @PostConstruct
//    public void initUsers() {
//        if (production == "prod") {
//            List<WarehouseUser> users = Stream.of(
//                    new WarehouseUser(0L, WarehouseCommonUtil.generateUserId(), "Randrino17", "Nzeukang Randrin", "Male", "nzeukangrandrin@gmail.com", "", bCryptPasswordEncoder.encode("123456789"), new LinkedHashSet<>(), "", true, WarehouseCommonUtil.generateCurrentDateUtil(), WarehouseCommonUtil.generateCurrentDateUtil()),
//                    new WarehouseUser(1L, WarehouseCommonUtil.generateUserId(), "Rodrigo", "Djomou Rodrigue", "Male", "djomoutresor1@hotmail.fr", "", bCryptPasswordEncoder.encode("123456789"), new LinkedHashSet<>(), "", true, WarehouseCommonUtil.generateCurrentDateUtil(), WarehouseCommonUtil.generateCurrentDateUtil())
//            ).collect(Collectors.toList());
//            userRepository.saveAll(users);
//        }
//    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/v1/warehouse/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry
//                        .addMapping("/v1/warehouse/**")
//                        .allowedOrigins("*")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE");
//            }
//        };
//    }
}
