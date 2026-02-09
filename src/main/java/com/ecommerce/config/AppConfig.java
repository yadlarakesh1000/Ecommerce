package com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration          //3:04:00
@EnableWebSecurity
public class AppConfig {
	@Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authorizeHttpRequests(Authorize -> Authorize
//	                		.requestMatchers("/api/admin/**").hasAnyRole("SHOP_OWNER","ADMIN")
	                                .requestMatchers("/api/**").authenticated()
	                                .requestMatchers("/api/products/*/reviews").permitAll()
	                                .anyRequest().permitAll()
	                )
	                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
	                .csrf(csrf -> csrf.disable())
	                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // why we need cors when we connect front with back browser throws cors exception so we handle those exception and it tells spring securty that this endpoint and this fronted url able to access our backend and this method  will be available 
	               
			
			return http.build();
			
		}

	private CorsConfigurationSource corsConfigurationSource() {
		  return new CorsConfigurationSource() {
	            @Override
	            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	         return null;
	            }
	        };
		
	}

}
