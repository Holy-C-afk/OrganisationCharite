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
            .authorizeHttpRequests(auth -> {
                logger.debug("Configuring authorization rules");
                auth
                    .requestMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**", "/h2-console/**", "/api/public/**", "/error").permitAll()
                    .requestMatchers("/admin/**").hasRole("SUPER_ADMIN")
                    .requestMatchers("/organization/**").hasAnyRole("ORGANIZATION_ADMIN", "SUPER_ADMIN")
                    .anyRequest().authenticated();
            })
            .formLogin(form -> {
                logger.debug("Configuring form login");
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureUrl("/login?error=true")
                    .permitAll();
            })
            .logout(logout -> {
                logger.debug("Configuring logout");
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll();
            })
            .exceptionHandling(exception -> {
                logger.debug("Configuring exception handling");
                exception
                    .accessDeniedPage("/error")
                    .authenticationEntryPoint((request, response, authException) -> {
                        logger.warn("Unauthorized access attempt to {}", request.getRequestURI());
                        response.sendRedirect("/login");
                    });
            })
            .csrf(csrf -> {
                logger.debug("Configuring CSRF protection");
                csrf
                    .ignoringRequestMatchers("/h2-console/**", "/api/**")
                    .requireCsrfProtectionMatcher(request -> {
                        String method = request.getMethod();
                        return !method.equals("GET") && !method.equals("HEAD") && !method.equals("TRACE") && !method.equals("OPTIONS");
                    });
            })
            .headers(headers -> {
                logger.debug("Configuring security headers");
                headers
                    .frameOptions().sameOrigin()
                    .xssProtection();
            });

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