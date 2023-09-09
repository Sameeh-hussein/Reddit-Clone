package com.sameeh.springit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(EndpointRequest.to("info")).permitAll()
                                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                                .requestMatchers("/actuator/").hasRole("ACTUATOR")
                                .requestMatchers("/link/submit").hasRole("USER") // just users can access this
                                .requestMatchers("/", "/link/**").permitAll() // can anyone access this page
                                .requestMatchers("/css/**", "/images/**", "/libs/bootstrap/**").permitAll() // to access the css and bootstrap
                                .requestMatchers("/", "/link/**", "/register", "/activate/**").permitAll() // can anyone access this page
                                .anyRequest().authenticated() // all other pages are protected

                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login").permitAll()
                                .usernameParameter("email")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // redirect to login with logout parameter
                        .permitAll()
                )
                .rememberMe(Customizer.withDefaults());
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
//                .headers(headers ->
//                        headers
//                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
//                                )
//                );
        return http.build();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

}