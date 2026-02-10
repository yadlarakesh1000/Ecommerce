package com.ecommerce.config;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  // sessionmanagemnt is stateless because we are using JWT token for authentication and authorization, so we don't need to maintain any session on the server side.
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(Authorize -> Authorize
//                		.requestMatchers("/api/admin/**").hasAnyRole("SHOP_OWNER","ADMIN")
                                .requestMatchers("/api/**").authenticated()
                                .requestMatchers("/api/products/*/reviews").permitAll()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                //why we need to configure CORS here because we are using JWT token for authentication and authorization, so we need to allow cross-origin requests from the client side to the server side.
                //which if we don't configure CORS then we will get CORS error in the fronted when we try to make a request to the backend .
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
               
		
		return http.build();
		
	}
	
    // CORS Configuration
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Arrays.asList(
                        "https://zosh-bazzar-zosh.vercel.app",
                        "http://localhost:3000",
                        "http://localhost:5173"));
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setExposedHeaders(Arrays.asList("Authorization"));
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

