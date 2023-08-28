package com.feeManagmentSystem.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers("/resources/**").permitAll();
                    authConfig.requestMatchers(  "/","/admin/**","/accountant/**").permitAll();
                    authConfig.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage("/accountant/");
                    login.defaultSuccessUrl("/accountant/home").permitAll();
                })
                .formLogin(login -> {
                    login.loginPage("/admin/");
                    login.defaultSuccessUrl("/admin/home").permitAll();
                })
                .logout(logout -> {
                    logout.logoutSuccessUrl("/").permitAll();
                });
        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
