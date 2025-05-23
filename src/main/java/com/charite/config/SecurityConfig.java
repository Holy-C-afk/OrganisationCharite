package com.charite.config;

import com.charite.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring security filter chain");
        
        http
            .authorizeHttpRequests(authorize -> authorize
                // Public resources
                .requestMatchers(
                    "/",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico",
                    "/test.html"
                ).permitAll()
                
                // Public pages
                .requestMatchers(
                    "/login",
                    "/register",
                    "/about",
                    "/contact",
                    "/charity-actions/**",
                    "/organizations/**",
                    "/donations",
                    "/donations/**"
                ).permitAll()
                
                // Admin pages
                .requestMatchers(
                    "/admin/**"
                ).hasAuthority("ROLE_SUPER_ADMIN")
                
                // Organization admin pages
                .requestMatchers(
                    "/organization/**"
                ).hasAuthority("ROLE_ORGANIZATION_ADMIN")
                
                // User pages
                .requestMatchers(
                    "/profile/**"
                ).hasAuthority("ROLE_USER")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            );

        logger.debug("Security filter chain configuration completed");
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            logger.debug("Loading user details for email: {}", email);
            return userService.getUserByEmail(email)
                .map(user -> {
                    logger.debug("User found: {}", user.getEmail());
                    return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );
                })
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        };
    }
} 